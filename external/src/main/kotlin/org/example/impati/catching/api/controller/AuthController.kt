package org.example.impati.catching.api.controller

import org.example.impati.catching.api.request.ExchangeCodeRequest
import org.example.impati.catching.api.request.RefreshTokenRequest
import org.example.impati.catching.api.response.AuthUrlResponse
import org.example.impati.catching.api.response.MemberResponse
import org.example.impati.catching.api.response.TokenResponse
import org.example.impati.catching.auth.AuthMode
import org.example.impati.catching.auth.MemberClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val memberClient: MemberClient
) {

    @GetMapping("/v1/auth/gateway")
    fun gateway(
        @RequestParam(defaultValue = "login") mode: String,
    ): AuthUrlResponse {
        return AuthUrlResponse(memberClient.gateway(AuthMode.from(mode)));
    }

    @PostMapping("/v1/auth/code")
    fun exchangeCode(
        @RequestBody request: ExchangeCodeRequest,
    ): TokenResponse {
        return TokenResponse.from(
            memberClient.exchangeCode(
                code = request.code,
                clientId = request.clientId
            )
        )
    }

    @PostMapping("/v1/auth/token/refresh")
    fun refresh(
        @RequestBody request: RefreshTokenRequest,
    ): TokenResponse {
        return TokenResponse.from(
            memberClient.refresh(request.refreshToken)
        )
    }

    @GetMapping("/v1/auth/me")
    fun me(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String): MemberResponse {
        return MemberResponse.from(
            memberClient.findMember(authorization)
        )
    }
}
