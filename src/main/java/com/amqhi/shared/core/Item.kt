package com.amqhi.shared.core

import java.util.UUID

data class Item(
    val id: UUID,
    val type: Char,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val eventAt: Long? = null,
    val deletedAt: Long? = null,
    val parentId: UUID,
    val name: String,
    val comment: String?,
    val encrypted: Boolean = false,
    val status: Char,
    val appScope: Short = 63
)