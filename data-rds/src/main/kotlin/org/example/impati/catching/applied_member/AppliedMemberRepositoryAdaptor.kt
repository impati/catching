package org.example.impati.catching.applied_member

import org.example.impati.catching.applied_member.exception.NotFoundAppliedMemberException
import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstCome
import org.springframework.stereotype.Repository

@Repository
class AppliedMemberRepositoryAdaptor(
    private val appliedMemberEntityRepository: AppliedMemberEntityRepository,
) : AppliedMemberRepository {

    override fun save(appliedMember: AppliedMember): AppliedMember {
        val entity = appliedMemberEntityRepository.findByFirstComeIdAndMemberId(
            firstComeId = appliedMember.comeId,
            memberId = appliedMember.memberId,
        )?.update(appliedMember) ?: AppliedMemberEntity.from(appliedMember)

        return appliedMemberEntityRepository.save(entity).toDomain()
    }

    override fun findBy(
        member: Member,
        firstCome: FirstCome,
    ): AppliedMember {
        return appliedMemberEntityRepository.findByFirstComeIdAndMemberId(
            firstComeId = firstCome.id,
            memberId = member.id,
        )?.toDomain() ?: throw NotFoundAppliedMemberException()
    }
}
