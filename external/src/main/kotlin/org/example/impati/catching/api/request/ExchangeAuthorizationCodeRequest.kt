package org.example.impati.catching.api.request

data class ExchangeAuthorizationCodeRequest(
    val clientId: String,
    val redirectUrl: String,
    val code: String,
)
