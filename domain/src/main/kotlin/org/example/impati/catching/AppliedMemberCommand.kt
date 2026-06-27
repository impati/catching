package org.example.impati.catching

import org.example.impati.catching.applied_member.AppliedMember
import org.example.impati.catching.applied_member.AppliedMemberRepository
import org.example.impati.catching.applied_member.exception.RequiredFieldException
import org.example.impati.catching.auth.Member
import org.example.impati.catching.field.FieldRepository
import org.example.impati.catching.field.Information
import org.example.impati.catching.first_come.FirstCome
import org.springframework.stereotype.Component

@Component
class AppliedMemberCommand(
    private val appliedMemberRepository: AppliedMemberRepository,
    private val fieldRepository: FieldRepository,
) {

    fun create(firstCome: FirstCome, member: Member, informations: List<Information>): AppliedMember {
        informations.forEach {
            val field = fieldRepository.findByField(it.name)
            if (field.required && it.values.isEmpty()) {
                throw RequiredFieldException()
            }
        }

        return appliedMemberRepository.save(
            AppliedMember(
                firstCome.id,
                member.id,
                informations
            )
        )
    }
}
