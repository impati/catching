package org.example.impati.catching.auth.exception

import org.example.impati.catching.support.ErrorCode

class ExpiredTokenException(
) : MemberException("엑세스 토큰이 만료되었습니다.", ErrorCode.EXPIRED_TOKEN) {
}
