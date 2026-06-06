package org.example.impati.catching.applied_event

import org.springframework.data.jpa.repository.JpaRepository

interface AppliedEventEntityRepository : JpaRepository<AppliedEventEntity, String> {

    fun countByFirstComeId(firstComeId: String): Long

    fun existsByFirstComeIdAndMemberId(firstComeId: String, memberId: String): Boolean

    fun findByFirstComeIdAndMemberId(firstComeId: String, memberId: String): AppliedEventEntity?
}
