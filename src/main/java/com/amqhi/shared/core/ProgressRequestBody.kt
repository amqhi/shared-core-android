package com.amqhi.shared.core

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File

class ProgressRequestBody(
    private val file: File,
    private val headers: Map<String, String>,
    private val onProgress: (Long, Long) -> Unit
) : RequestBody() {
    override fun contentType() = headers["Content-Type"]?.toMediaType()
    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val totalBytes = file.length()
        var uploadedBytes = 0L
        file.source().use { source ->
            var bytesRead: Long
            var lastProgressTime = 0L
            while (source.read(sink.buffer, 8_192L).also { bytesRead = it } != -1L) {
                sink.flush()
                uploadedBytes += bytesRead
                val currentTime = System.currentTimeMillis()
                if(totalBytes > 0 && (currentTime - lastProgressTime > 100 || uploadedBytes == totalBytes)) {
                    lastProgressTime = currentTime
                    onProgress(uploadedBytes, totalBytes)
                }
            }
        }
    }
}