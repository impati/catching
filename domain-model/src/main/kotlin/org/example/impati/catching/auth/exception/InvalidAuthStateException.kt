package org.example.impati.catching.auth.exception

import org.example.impati.catching.support.ErrorCode

class InvalidAuthStateException : MemberException("인증 요청 상태가 유효하지 않습니다.", ErrorCode.INVALID_AUTH_STATE)
