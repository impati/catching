package org.example.impati.catching.applied_member

import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstCome

interface AppliedMemberRepository {

    fun save(appliedMember: AppliedMember): AppliedMember

    fun update(appliedMember: AppliedMember): AppliedMember

    fun exists(member: Member, firstCome: FirstCome): Boolean

    fun findBy(member: Member, firstCome: FirstCome): AppliedMember?
}
