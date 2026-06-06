package org.example.impati.catching

import org.example.impati.catching.applied_event.AlternateEvent
import org.example.impati.catching.applied_event.AlternateEventRepository
import org.example.impati.catching.applied_event.AppliedEvent
import org.example.impati.catching.applied_event.AppliedEventRepository
import org.example.impati.catching.applied_event.exception.DuplicationApplyForEvent
import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.ActiveFirstCome
import org.example.impati.catching.first_come.WaitType
import org.example.impati.catching.support.LockSection
import org.springframework.stereotype.Component

@Component
class AppliedEventCommand(
    val appliedEventRepository: AppliedEventRepository,
    val alternateEventRepository: AlternateEventRepository,
    val lockSection: LockSection
) {

    fun applyFor(firstCome: ActiveFirstCome, member: Member) {
        lockSection.execute("first-come-${firstCome.id}", 10) {

            when {
                firstCome.eligibility.duplicable -> {
                    if (appliedEventRepository.exists(firstCome, member)) {
                        throw DuplicationApplyForEvent()
                    }
                }
            }

            val appliedCount = appliedEventRepository.count(firstCome)
            if (appliedCount < firstCome.capacity.value) {
                appliedEventRepository.save(AppliedEvent(firstCome, member))
            } else {
                if (firstCome.waitPolicy.waitType == WaitType.WAITLIST) {
                    val alternatedCount = alternateEventRepository.count(firstCome)
                    if (alternatedCount < firstCome.waitPolicy.capacity!!) {
                        alternateEventRepository.save(AlternateEvent(firstCome, member))
                    }
                }
            }
        }
    }
}
