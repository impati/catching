package org.example.impati.catching.api.response

import org.example.impati.catching.auth.Token

data class TokenResponse(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String,
) {
    companion object {
        fun from(token: Token): TokenResponse {
            return TokenResponse(
                token.accessToken,
                token.expiresIn,
                token.refreshToken,
            )
        }
    }
}
