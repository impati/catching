package org.example.impati.catching.api.request

data class ExchangeCodeRequest(
    val code: String,
    val clientId: String,
)
