package com.amqhi.shared.core

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.UnrecoverableKeyException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

open class SecureStorageProviderBase(context: Context) {
    protected val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    protected val lock = Any()
    protected val useKeystore = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    @Volatile
    protected var secretKey: SecretKey? = null

    open fun setSecureString(key: String, value: String): Boolean {
        if (!useKeystore) {
            return synchronized(lock) { putPlain(key, value) }
        }
        return try {
            synchronized(lock) {
                prefs.edit().putString(key, encrypt(value)).commit()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to store secure string", e)
            false
        }
    }

    open fun getSecureString(key: String, defaultValue: String): String {
        synchronized(lock) {
            val stored = prefs.getString(key, null) ?: return defaultValue
            if (!useKeystore) return stored
            return try {
                decrypt(stored)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to read secure string", e)
                if (e.isDiscardableCiphertext() && !prefs.edit().remove(key).commit()) {
                    Log.e(TAG, "Failed to remove secure string")
                }
                defaultValue
            }
        }
    }

    open fun removeSecureString(key: String) {
        synchronized(lock) {
            if (!prefs.edit().remove(key).commit()) {
                Log.e(TAG, "Failed to remove secure string")
            }
        }
    }

    protected open fun putPlain(key: String, value: String): Boolean {
        val stored = prefs.edit().putString(key, value).commit()
        if (!stored) {
            Log.e(TAG, "Failed to store secure string")
        }
        return stored
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected open fun getOrCreateKey(): SecretKey {
        secretKey?.let { return it }
        synchronized(lock) {
            secretKey?.let { return it }
            val keyStore = keyStore()
            if (keyStore.containsAlias(KEY_ALIAS)) {
                try {
                    val existing = (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey
                    if (existing != null) {
                        secretKey = existing
                        return existing
                    }
                    Log.e(TAG, "Secure key entry missing, resetting")
                    resetKeyMaterial()
                } catch (e: Exception) {
                    if (!e.isKeyUnavailable()) throw e
                    Log.e(TAG, "Secure key unusable, resetting", e)
                    resetKeyMaterial()
                }
            }
            return generateKey().also { secretKey = it }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected open fun generateKey(): SecretKey {
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        generator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setRandomizedEncryptionRequired(true)
                .build()
        )
        return generator.generateKey()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected open fun initCipher(mode: Int, key: SecretKey, spec: GCMParameterSpec?): Cipher {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        if (spec == null) {
            cipher.init(mode, key)
        } else {
            cipher.init(mode, key, spec)
        }
        return cipher
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected open fun cipherForEncrypt(): Cipher {
        return try {
            initCipher(Cipher.ENCRYPT_MODE, getOrCreateKey(), null)
        } catch (e: Exception) {
            if (!e.isKeyUnavailable()) throw e
            Log.e(TAG, "Secure key unusable, resetting", e)
            resetKeyMaterial()
            initCipher(Cipher.ENCRYPT_MODE, getOrCreateKey(), null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected open fun encrypt(value: String): String {
        val cipher = cipherForEncrypt()
        val plaintext = value.toByteArray(Charsets.UTF_8)
        val (iv, ciphertext) = try {
            cipher.iv to cipher.doFinal(plaintext)
        } catch (e: Exception) {
            if (!e.isKeyUnavailable()) throw e
            Log.e(TAG, "Secure key unusable, resetting", e)
            resetKeyMaterial()
            val retry = initCipher(Cipher.ENCRYPT_MODE, getOrCreateKey(), null)
            retry.iv to retry.doFinal(plaintext)
        }
        return pack(iv, ciphertext)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected open fun decrypt(encryptedValue: String): String {
        val (iv, ciphertext) = split(encryptedValue)
        val cipher = try {
            initCipher(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(GCM_TAG_BITS, iv)
            )
        } catch (e: Exception) {
            if (!e.isKeyUnavailable()) throw e
            Log.e(TAG, "Secure key unusable, resetting", e)
            resetKeyMaterial()
            throw e
        }
        try {
            return String(cipher.doFinal(ciphertext), Charsets.UTF_8)
        } catch (e: Exception) {
            if (!e.isKeyUnavailable()) throw e
            Log.e(TAG, "Secure key unusable, resetting", e)
            resetKeyMaterial()
            throw e
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected open fun resetKeyMaterial() {
        secretKey = null
        deleteKeyAlias()
        if (!prefs.edit().clear().commit()) {
            Log.e(TAG, "Failed to clear secure prefs")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected open fun deleteKeyAlias() {
        try {
            val keyStore = keyStore()
            if (keyStore.containsAlias(KEY_ALIAS)) {
                keyStore.deleteEntry(KEY_ALIAS)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete secure key", e)
        }
    }

    protected open fun keyStore(): KeyStore {
        return KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
    }

    protected open fun pack(iv: ByteArray, ciphertext: ByteArray): String {
        val payload = ByteArray(FORMAT_MAGIC.size + iv.size + ciphertext.size)
        FORMAT_MAGIC.copyInto(payload, destinationOffset = 0)
        iv.copyInto(payload, destinationOffset = FORMAT_MAGIC.size)
        ciphertext.copyInto(payload, destinationOffset = FORMAT_MAGIC.size + iv.size)
        return Base64.encodeToString(payload, Base64.NO_WRAP)
    }

    protected open fun split(encryptedValue: String): Pair<ByteArray, ByteArray> {
        val decoded = Base64.decode(encryptedValue, Base64.NO_WRAP)
        if (decoded.size < FORMAT_MAGIC.size + GCM_IV_LENGTH + GCM_TAG_BYTES) {
            throw IllegalArgumentException("Unrecognized secure storage format")
        }
        for (index in FORMAT_MAGIC.indices) {
            if (decoded[index] != FORMAT_MAGIC[index]) {
                throw IllegalArgumentException("Unrecognized secure storage format")
            }
        }
        val ivOffset = FORMAT_MAGIC.size
        val ciphertextOffset = ivOffset + GCM_IV_LENGTH
        return decoded.copyOfRange(ivOffset, ciphertextOffset) to
                decoded.copyOfRange(ciphertextOffset, decoded.size)
    }

    protected open fun Throwable.isKeyUnavailable(): Boolean {
        return this is UnrecoverableKeyException ||
                this is KeyPermanentlyInvalidatedException ||
                this is InvalidKeyException
    }

    protected open fun Throwable.isDiscardableCiphertext(): Boolean {
        return this is IllegalArgumentException ||
                this is BadPaddingException ||
                this is IllegalBlockSizeException
    }

    protected companion object {
        const val TAG = "SecureStorageProvider"
        const val PREFS_NAME = "secure_prefs"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "secure_storage_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_IV_LENGTH = 12
        const val GCM_TAG_BITS = 128
        const val GCM_TAG_BYTES = GCM_TAG_BITS / 8
        val FORMAT_MAGIC = byteArrayOf(0x41, 0x51, 0x47, 0x31)
    }
}