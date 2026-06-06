package org.example.impati.catching.first_come

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class FirstComeRepositoryAdaptor(
    private val firstComeEntityRepository: FirstComeEntityRepository
) : FirstComeRepository {

    override fun save(createdFirstCome: CreatedFirstCome): CreatedFirstCome {
        return firstComeEntityRepository.save(FirstComeEntity.from(createdFirstCome)).toCreated();
    }

    override fun save(approvedFirstCome: ApprovedFirstCome): ApprovedFirstCome {
        return firstComeEntityRepository.save(FirstComeEntity.from(approvedFirstCome)).toApproved()
    }

    override fun findById(id: String): Optional<CreatedFirstCome> {
        return firstComeEntityRepository.findById(id).map { it.toCreated() };
    }

    override fun findActiveById(id: String, now: LocalDateTime): Optional<ActiveFirstCome> {
        return firstComeEntityRepository.findById(id).map { it.toActive(now) }
    }

    override fun findAll(): List<CreatedFirstCome> {
        return firstComeEntityRepository.findAll().map { it.toCreated() }
    }

    override fun findActiveBy(now: LocalDateTime): List<ActiveFirstCome> {
        return firstComeEntityRepository.findActiveBy(now).map { it.toActive(now) }
    }
}
