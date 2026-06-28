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
        return appliedMemberEntityRepository.save(AppliedMemberEntity.from(appliedMember)).toDomain()
    }

    override fun update(appliedMember: AppliedMember): AppliedMember {
        val entity = appliedMemberEntityRepository.findByFirstComeIdAndMemberId(
            firstComeId = appliedMember.comeId,
            memberId = appliedMember.memberId,
        )?.update(appliedMember) ?: throw NotFoundAppliedMemberException()

        return appliedMemberEntityRepository.save(entity).toDomain()
    }

    override fun exists(
        member: Member,
        firstCome: FirstCome,
    ): Boolean {
        return appliedMemberEntityRepository.existsByFirstComeIdAndMemberId(
            firstComeId = firstCome.id,
            memberId = member.id,
        )
    }

    override fun findBy(
        member: Member,
        firstCome: FirstCome,
    ): AppliedMember? {
        return appliedMemberEntityRepository.findByFirstComeIdAndMemberId(
            firstComeId = firstCome.id,
            memberId = member.id,
        )?.toDomain()
    }
}
