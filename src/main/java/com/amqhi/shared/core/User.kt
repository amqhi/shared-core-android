package com.amqhi.shared.core

data class User(
    val localId: Char,
    val name: String,
    val email: String,
    val loginType: Short,
    val cachedPicturePath: String?
)