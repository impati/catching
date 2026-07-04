package org.example.impati.catching.support

import org.example.impati.catching.applied_event.exception.NotFoundAppliedEvent
import org.example.impati.catching.applied_member.exception.NotFoundAppliedMemberException
import org.example.impati.catching.auth.exception.ExpiredTokenException
import org.example.impati.catching.member_agreement.exception.RequiredAgreementException
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

    @ExceptionHandler(NotFoundAppliedEvent::class, NotFoundAppliedMemberException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundExceptionHandle(e: RuntimeException): ErrorResponse {
        return ErrorResponse(
            e.message ?: "not found",
            ErrorCode.NOT_FOUND
        )
    }

    @ExceptionHandler(RequiredAgreementException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun requiredAgreementExceptionHandle(e: RequiredAgreementException): ErrorResponse {
        return ErrorResponse(
            e.message ?: "required agreement",
            ErrorCode.REQUIRED_AGREEMENT
        )
    }
}
