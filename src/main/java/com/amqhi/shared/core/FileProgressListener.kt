package com.amqhi.shared.core

import java.util.UUID

interface FileProgressListener {
    fun onDownloadStarted(itemId: UUID, parentId: UUID) {}
    fun onUploadStarted(itemId: UUID, parentId: UUID) {}
    fun onDownloadProgress(itemId: UUID, parentId: UUID, bytesReceived: Long, totalBytes: Long) {}
    fun onUploadProgress(itemId: UUID, parentId: UUID, name: String, bytesWritten: Long, totalBytes: Long) {}

    fun onDownloadFinished(itemId: UUID, parentId: UUID) {}
    fun onUploadFinished(itemId: UUID, parentId: UUID) {}
}