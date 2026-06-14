package org.example.impati.catching

import org.example.impati.catching.applied_event.AppliedEvent
import org.example.impati.catching.applied_event.AppliedEventRepository
import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstCome
import org.springframework.stereotype.Component

@Component
class AppliedEventQuery(
    val appliedEventRepository: AppliedEventRepository
) {

    fun getAppliedEvent(firstCome: FirstCome, member: Member): AppliedEvent {
        val appliedEvent = appliedEventRepository.findBy(firstCome, member)
            ?: throw IllegalArgumentException("Applied event not found for first come: $firstCome and member: $member")

        return appliedEvent
    }
}
