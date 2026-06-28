package org.example.impati.catching.terms.exception

import org.example.impati.catching.support.ErrorCode

open class NotFoundTermsGroupException(
    message: String,
    val code: ErrorCode = ErrorCode.NOT_FOUND,
) : RuntimeException(message) {
}
