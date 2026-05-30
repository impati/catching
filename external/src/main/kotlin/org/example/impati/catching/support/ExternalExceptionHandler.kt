package org.example.impati.catching.support

import org.example.impati.catching.auth.exception.ExpiredTokenException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ExternalExceptionHandler {

    @ExceptionHandler(ExpiredTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun memberExceptionHandle(e: ExpiredTokenException): ErrorResponse {
        return ErrorResponse(
            e.message!!,
            e.code
        );
    }
}
