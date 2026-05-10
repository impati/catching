package org.example.impati.catching.api.response

import org.example.impati.catching.auth.Member

data class MemberResponse(
    val memberId: String,
    val nickName: String,
) {

    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                member.id,
                member.nickName
            )
        }
    }
}
