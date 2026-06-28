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

    fun notExist(firstCome: FirstCome, member: Member): Boolean {
        return findAppliedEvent(firstCome, member) == null;
    }

    fun findAppliedEvent(firstCome: FirstCome, member: Member): AppliedEvent? {
        return appliedEventRepository.findBy(firstCome, member)
    }

    fun countAppliedEvents(firstCome: FirstCome): Int {
        return appliedEventRepository.count(firstCome);
    }
}
