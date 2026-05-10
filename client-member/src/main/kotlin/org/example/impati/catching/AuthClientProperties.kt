package org.example.impati.catching

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "catching.auth")
data class AuthClientProperties(
    val memberApiBaseUrl: String = "http://localhost:8081",
    val memberWebBaseUrl: String = "http://localhost:3000/index.html",
    val clientId: String = "catch",
    val webRedirectUrl: String = "http://localhost:5173/",
)
