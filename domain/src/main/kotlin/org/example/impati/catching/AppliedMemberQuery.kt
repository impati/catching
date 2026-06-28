package org.example.impati.catching

import org.example.impati.catching.applied_member.AppliedMember
import org.example.impati.catching.applied_member.AppliedMemberRepository
import org.example.impati.catching.applied_member.exception.NotFoundAppliedMemberException
import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstCome
import org.springframework.stereotype.Component

@Component
class AppliedMemberQuery(
    private val appliedMemberRepository: AppliedMemberRepository
) {

    fun notExists(firstCome: FirstCome, member: Member): Boolean {
        return !exists(firstCome, member)
    }

    fun exists(firstCome: FirstCome, member: Member): Boolean {
        return appliedMemberRepository.findBy(member, firstCome) != null;
    }

    fun getAppliedMember(firstCome: FirstCome, member: Member): AppliedMember {
        return appliedMemberRepository.findBy(member, firstCome) ?: throw NotFoundAppliedMemberException()
    }
}
