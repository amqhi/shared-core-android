package com.amqhi.shared.core

object ItemStatus {
    const val AVAILABLE_OFFLINE: Char = 'a'
    const val ONLINE_ONLY: Char = 'o'
    const val PENDING_UPLOAD: Char = 'p'
    const val UPLOADING: Char = 'u'
}