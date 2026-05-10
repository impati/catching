package org.example.impati.catching.auth

interface MemberClient {

    fun gateway(authMode: AuthMode): String

    fun exchangeCode(code: String, clientId: String): Token

    fun refresh(refreshToken: String): Token

    fun findMember(accessToken: String): Member
}
