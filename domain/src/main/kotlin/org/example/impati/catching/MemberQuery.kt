package org.example.impati.catching

import org.example.impati.catching.auth.Member
import org.example.impati.catching.auth.MemberClient
import org.springframework.stereotype.Component

@Component
class MemberQuery(
    val memberClient: MemberClient
) {

    fun getMember(accessToken: String): Member {
        return memberClient.findMember(accessToken);
    }
}
