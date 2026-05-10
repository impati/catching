package org.example.impati.catching.auth

enum class AuthMode(
    val queryValue: String,
) {
    LOGIN("login"),
    SIGNUP("signup");

    companion object {

        fun from(value: String): AuthMode {
            return entries.firstOrNull {
                it.name.equals(
                    value,
                    ignoreCase = true
                ) || it.queryValue == value.lowercase()
            }
                ?: throw IllegalArgumentException("지원하지 않는 인증 모드입니다: $value")
        }
    }
}
