package org.example.impati.catching

import org.example.impati.catching.auth.*
import org.example.impati.catching.auth.exception.InvalidAuthStateException
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration
import java.util.*

@Component
class AuthExecutor(
    val memberAuthClient: MemberAuthClient,
    val properties: AuthClientProperties,
    val sessionStore: SessionStore
) : AuthUseCase {

    private val sessionTtl = Duration.ofMinutes(10)

    override fun createSession(): String {
        val sessionId = UUID.randomUUID().toString()
        sessionStore.save(sessionId, UUID.randomUUID().toString(), sessionTtl)
        return sessionId
    }

    override fun gateway(mode: AuthMode, sessionId: String): String {
        val builder = UriComponentsBuilder.fromUriString(properties.memberWebBaseUrl)
            .queryParam("clientId", properties.clientId)
            .queryParam("redirectUrl", properties.webRedirectUrl)
            .queryParam("state", sessionStore.find(sessionId))

        if (mode == AuthMode.SIGNUP) {
            builder.queryParam("mode", mode.queryValue)
        }

        return builder.build()
            .encode()
            .toUriString()
    }

    override fun exchange(code: String, sessionId: String, state: String): Token {
        if (sessionStore.find(sessionId) != state) {
            throw InvalidAuthStateException()
        }

        sessionStore.delete(sessionId)
        return memberAuthClient.exchangeCode(code, properties.clientId)
    }

    override fun refresh(refreshToken: String): Token {
        return memberAuthClient.refresh(refreshToken)
    }

    override fun findMember(accessToken: String): Member {
        return memberAuthClient.findMember(accessToken)
    }
}
