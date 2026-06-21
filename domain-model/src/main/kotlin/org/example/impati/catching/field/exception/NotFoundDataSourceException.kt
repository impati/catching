package org.example.impati.catching.field.exception

import org.example.impati.catching.support.ErrorCode

open class NotFoundDataSourceException(
    message: String,
    val code: ErrorCode = ErrorCode.NOT_FOUND,
) : RuntimeException(message) {
}
