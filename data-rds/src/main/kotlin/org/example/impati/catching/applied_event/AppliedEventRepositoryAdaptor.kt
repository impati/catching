package org.example.impati.catching.applied_event

import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstCome
import org.springframework.stereotype.Repository

@Repository
class AppliedEventRepositoryAdaptor(
    private val appliedEventEntityRepository: AppliedEventEntityRepository,
) : AppliedEventRepository {
    override fun save(appliedEvent: AppliedEvent) {
        appliedEventEntityRepository.save(AppliedEventEntity.from(appliedEvent))
    }

    override fun count(firstCome: FirstCome): Int {
        return appliedEventEntityRepository.countByFirstComeId(firstCome.id).toInt()
    }

    override fun exists(
        firstCome: FirstCome,
        member: Member
    ): Boolean {
        return appliedEventEntityRepository.existsByFirstComeIdAndMemberId(
            firstComeId = firstCome.id,
            memberId = member.id
        )
    }

    override fun findBy(
        firstCome: FirstCome,
        member: Member
    ): AppliedEvent? {
        return appliedEventEntityRepository.findByFirstComeIdAndMemberId(
            firstComeId = firstCome.id,
            memberId = member.id
        )?.let { AppliedEvent(firstCome, member) }
    }

}
