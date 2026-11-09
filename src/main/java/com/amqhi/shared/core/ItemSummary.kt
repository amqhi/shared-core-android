package com.amqhi.shared.core

import java.util.UUID

data class ItemSummary(
    val id: UUID,
    val type: Char,
    val name: String,
    val thumbnailPath: String,
    val iconType: Char,
    val status: Char
)