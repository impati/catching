package org.example.impati.catching.auth

data class Token(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String,
)
