package org.example.impati.catching.auth.exception

import org.example.impati.catching.support.ErrorCode

open class MemberException(
    message: String,
    val code: ErrorCode,
) : RuntimeException(message) {
}
