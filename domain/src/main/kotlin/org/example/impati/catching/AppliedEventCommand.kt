package org.example.impati.catching

import org.example.impati.catching.applied_event.AppliedEvent
import org.example.impati.catching.applied_event.AppliedEventCacheRepository
import org.example.impati.catching.applied_event.AppliedEventRepository
import org.example.impati.catching.applied_event.exception.ApplyFailException
import org.example.impati.catching.applied_event.exception.DuplicationApplyForEvent
import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.ActiveFirstCome
import org.example.impati.catching.support.LockSection
import org.springframework.stereotype.Component

@Component
class AppliedEventCommand(
    val appliedEventRepository: AppliedEventRepository,
    val appliedEventCacheRepository: AppliedEventCacheRepository,
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
                throw ApplyFailException()
            }
        }
    }

    fun applyForVer2(firstCome: ActiveFirstCome, member: Member) {
        val added = appliedEventCacheRepository.add(firstCome)
        if (added > firstCome.capacity.value) {
            appliedEventCacheRepository.minus(firstCome)
            throw ApplyFailException()
        }


        try {
            when {
                firstCome.eligibility.duplicable -> {
                    if (appliedEventRepository.exists(firstCome, member)) {
                        throw DuplicationApplyForEvent()
                    }
                }
            }

            appliedEventRepository.save(AppliedEvent(firstCome, member))
        } catch (e: Exception) {
            appliedEventCacheRepository.minus(firstCome)
            throw e
        }
    }
}
