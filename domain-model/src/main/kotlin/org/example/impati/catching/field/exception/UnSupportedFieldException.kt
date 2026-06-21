package org.example.impati.catching.field.exception

import org.example.impati.catching.support.ErrorCode

open class UnSupportedFieldException(
    message: String,
    val code: ErrorCode = ErrorCode.UNSUPPORTED_FIELD,
) : RuntimeException(message) {
}
