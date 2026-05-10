package org.example.impati.catching

import org.example.impati.catching.auth.AuthMode
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class AuthUrlFactory(
    private val properties: AuthClientProperties,
) {

    fun create(mode: AuthMode): String {
        val builder = UriComponentsBuilder.fromUriString(properties.memberWebBaseUrl)
            .queryParam("clientId", properties.clientId)
            .queryParam("redirectUrl", properties.webRedirectUrl)

        if (mode == AuthMode.SIGNUP) {
            builder.queryParam("mode", mode.queryValue)
        }

        return builder.build()
            .encode()
            .toUriString()
    }
}
