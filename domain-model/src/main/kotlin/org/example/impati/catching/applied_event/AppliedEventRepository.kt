package org.example.impati.catching.applied_event

import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstCome

interface AppliedEventRepository {

    fun save(appliedEvent: AppliedEvent)

    fun count(firstCome: FirstCome): Int

    fun exists(firstCome: FirstCome, member: Member): Boolean

    fun findBy(firstCome: FirstCome, member: Member): AppliedEvent?
}
