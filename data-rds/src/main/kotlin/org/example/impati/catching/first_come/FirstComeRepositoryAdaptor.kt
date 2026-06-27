package org.example.impati.catching.first_come

import org.example.impati.catching.field.Field
import org.example.impati.catching.field.FieldRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class FirstComeRepositoryAdaptor(
    private val firstComeEntityRepository: FirstComeEntityRepository,
    private val fieldRepository: FieldRepository,
) : FirstComeRepository {

    override fun save(createdFirstCome: CreatedFirstCome): CreatedFirstCome {
        return firstComeEntityRepository.save(FirstComeEntity.from(createdFirstCome))
            .toCreated(createdFirstCome.fields);
    }

    override fun save(approvedFirstCome: ApprovedFirstCome): ApprovedFirstCome {
        return firstComeEntityRepository.save(FirstComeEntity.from(approvedFirstCome))
            .toApproved(approvedFirstCome.fields)
    }

    override fun findById(id: String): Optional<CreatedFirstCome> {
        return firstComeEntityRepository.findById(id).map { it.toCreated(it.findFields()) };
    }

    override fun findActiveById(id: String, now: LocalDateTime): Optional<ActiveFirstCome> {
        return firstComeEntityRepository.findById(id).map { it.toActive(now, it.findFields()) }
    }

    override fun findAll(): List<CreatedFirstCome> {
        return firstComeEntityRepository.findAll().map { it.toCreated(it.findFields()) }
    }

    override fun findActiveBy(now: LocalDateTime): List<ActiveFirstCome> {
        return firstComeEntityRepository.findActiveBy(now).map { it.toActive(now, it.findFields()) }
    }

    private fun FirstComeEntity.findFields(): List<Field> {
        return fields.map { fieldRepository.findByField(it) }
    }
}
