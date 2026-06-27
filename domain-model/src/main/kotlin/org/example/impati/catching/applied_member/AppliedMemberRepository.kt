package org.example.impati.catching.applied_member

import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstCome

interface AppliedMemberRepository {

    fun save(appliedMember: AppliedMember): AppliedMember

    fun findBy(member: Member, firstCome: FirstCome): AppliedMember
}
