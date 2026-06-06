package org.example.impati.catching.first_come

import java.time.LocalDateTime
import java.util.*

interface FirstComeRepository {

    fun save(createdFirstCome: CreatedFirstCome): CreatedFirstCome

    fun save(approvedFirstCome: ApprovedFirstCome): ApprovedFirstCome

    fun findById(id: String): Optional<CreatedFirstCome>

    fun findActiveById(id: String, now: LocalDateTime): Optional<ActiveFirstCome>

    fun findAll(): List<CreatedFirstCome>

    fun findActiveBy(now: LocalDateTime): List<ActiveFirstCome>
}
