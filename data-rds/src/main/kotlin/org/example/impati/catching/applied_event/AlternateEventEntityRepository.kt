package org.example.impati.catching.applied_event

import org.springframework.data.jpa.repository.JpaRepository

interface AlternateEventEntityRepository : JpaRepository<AlternateEventEntity, String> {

    fun countByFirstComeId(firstComeId: String): Long
}
