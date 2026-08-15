package org.example.impati.catching.api.controller

import jakarta.servlet.http.HttpServletResponse
import org.example.impati.catching.api.request.ExchangeCodeRequest
import org.example.impati.catching.api.request.RefreshTokenRequest
import org.example.impati.catching.api.response.AuthGatewayResponse
import org.example.impati.catching.api.response.MemberResponse
import org.example.impati.catching.api.response.TokenResponse
import org.example.impati.catching.auth.AuthMode
import org.example.impati.catching.auth.AuthUseCase
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.*
import java.time.Duration

@RestController
class AuthController(
    private val authUseCase: AuthUseCase
) {

    @GetMapping("/v1/auth/gateway")
    fun gateway(
        @RequestParam(defaultValue = "login") mode: String,
        response: HttpServletResponse
    ): AuthGatewayResponse {
        val session = authUseCase.createSession()
        val cookie = ResponseCookie.from("sessionId", session)
            .httpOnly(true)
            .secure(true)
            .sameSite("Lax")
            .path("/")
            .maxAge(Duration.ofMinutes(10))
            .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())

        return AuthGatewayResponse(
            authUseCase.gateway(AuthMode.from(mode), session)
        )
    }

    @PostMapping("/v1/auth/code")
    fun exchangeCode(
        @RequestBody request: ExchangeCodeRequest,
        @CookieValue(name = "sessionId", required = true) sessionId: String
    ): TokenResponse {
        return TokenResponse.from(
            authUseCase.exchange(request.code, sessionId, request.state)
        )
    }

    @PostMapping("/v1/auth/token/refresh")
    fun refresh(
        @RequestBody request: RefreshTokenRequest,
    ): TokenResponse {
        return TokenResponse.from(
            authUseCase.refresh(request.refreshToken)
        )
    }

    @GetMapping("/v1/auth/me")
    fun me(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String): MemberResponse {
        return MemberResponse.from(
            authUseCase.findMember(authorization)
        )
    }
}
