package org.example.impati.catching

import org.example.impati.catching.auth.Member
import org.example.impati.catching.auth.MemberClient
import org.example.impati.catching.auth.Token
import org.example.impati.catching.auth.exception.ExpiredTokenException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class MemberAuthClient(
    private val properties: AuthClientProperties,
    restClientBuilder: RestClient.Builder,
) : MemberClient {

    private val restClient: RestClient = restClientBuilder
        .baseUrl(properties.memberApiBaseUrl)
        .build()

    fun exchangeCode(
        code: String,
        clientId: String
    ): Token {
        return restClient.post()
            .uri("/api/v1/auth/clients/code")
            .body(
                ExchangeAuthorizationCodeRequest(
                    clientId = clientId,
                    redirectUrl = properties.webRedirectUrl,
                    code = code,
                )
            )
            .retrieve()
            .body(TokenClientResponse::class.java)
            ?.toDomain()
            ?: throw IllegalStateException("회원 서버의 토큰 교환 응답이 비어 있습니다.")
    }

    fun refresh(refreshToken: String): Token {
        return restClient.post()
            .uri("/api/v1/auth/token/refresh")
            .body(RefreshTokenClientRequest(token = refreshToken))
            .retrieve()
            .body(TokenClientResponse::class.java)
            ?.toDomain()
            ?: throw IllegalStateException("회원 서버의 토큰 갱신 응답이 비어 있습니다.")
    }

    override fun findMember(accessToken: String): Member {
        return restClient.get()
            .uri("/api/v1/auth/me")
            .header(HttpHeaders.AUTHORIZATION, bearer(accessToken))
            .exchange<Member> { _, res ->
                when (res.statusCode) {
                    HttpStatus.OK ->
                        res.bodyTo(MemberClientResponse::class.java)?.toDomain()
                            ?: throw IllegalStateException("회원 서버의 회원 정보 응답이 비어 있습니다.")

                    HttpStatus.UNAUTHORIZED ->
                        throw ExpiredTokenException()

                    else ->
                        throw IllegalStateException("회원 서버의 회원 정보 조회에 실패했습니다: ${res.statusCode}")
                }
            }
    }

    private fun bearer(accessToken: String): String {
        val normalized = accessToken.trim()
        return if (normalized.startsWith("Bearer ", ignoreCase = true)) {
            normalized
        } else {
            "Bearer $normalized"
        }
    }
}

private data class ExchangeAuthorizationCodeRequest(
    val clientId: String,
    val redirectUrl: String,
    val code: String,
)

private data class RefreshTokenClientRequest(
    val token: String,
)

private data class TokenClientResponse(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String,
) {
    fun toDomain(): Token {
        return Token(
            accessToken = accessToken,
            expiresIn = expiresIn,
            refreshToken = refreshToken,
        )
    }
}

private data class MemberClientResponse(
    val memberId: String,
    val nickName: String,
) {
    fun toDomain(): Member {
        return Member(
            id = memberId,
            nickName = nickName,
        )
    }
}
