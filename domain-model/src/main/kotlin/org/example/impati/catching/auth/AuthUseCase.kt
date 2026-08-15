package org.example.impati.catching.auth

interface AuthUseCase {

    fun createSession(): String

    fun gateway(mode: AuthMode, sessionId: String): String

    fun exchange(code: String, sessionId: String, state: String): Token

    fun refresh(refreshToken: String): Token

    fun findMember(accessToken: String): Member
}
