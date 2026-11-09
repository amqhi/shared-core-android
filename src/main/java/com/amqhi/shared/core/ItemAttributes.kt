package com.amqhi.shared.core

data class ItemAttributes(
    val name: String,
    val eventAt: Long? = null,
    val parentIdHigh: Long,
    val parentIdLow: Long,
    val encrypted: Boolean = false,
    val comment: String? = null,
    val appScope: Short = AppScope.ALL
)