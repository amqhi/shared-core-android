/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use(::load)
    }
}

fun cmakeDefinition(key: String): String? {
    val value = localProperties.getProperty(key)?.trim().orEmpty()
    return value.takeIf { it.isNotEmpty() }?.let { "-D$key=$it" }
}

// TODO: Enable backward compatibility builds for minSdk 16
val minimumSdkVersion = localProperties.getProperty("minSdk").toInt()
val targetSdkVersion = localProperties.getProperty("targetSdk").toInt()
android {
    namespace = "com.amqhi.shared.core"
    compileSdk {
        version = release(targetSdkVersion) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = minimumSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    //noinspection UseTomlInstead
    if(minimumSdkVersion >= 24) {
        implementation("androidx.core:core-ktx:1.19.0")
        implementation("androidx.appcompat:appcompat:1.7.1")
        implementation("com.google.android.material:material:1.14.0")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.3.0")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    }
    else {
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("androidx.activity:activity:1.8.2")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    }
}
