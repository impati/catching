package org.example.impati.catching

import org.example.impati.catching.field.FieldRepository
import org.example.impati.catching.first_come.ApprovedFirstCome
import org.example.impati.catching.first_come.CreatedFirstCome
import org.example.impati.catching.first_come.FirstComeInputVo
import org.example.impati.catching.first_come.FirstComeRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class FirstComeCommand(
    val firstComeRepository: FirstComeRepository,
    val fieldRepository: FieldRepository
) {

    fun create(input: FirstComeInputVo): CreatedFirstCome {
        val id = UUID.randomUUID().toString().substring(0, 7);
        val field = input.fields.map { fieldRepository.findByField(it) }

        val createdFirstCome = firstComeRepository.save(
            CreatedFirstCome(
                id,
                input.name,
                input.capacity,
                input.time,
                input.eligibility,
                input.join,
                input.waitPolicy,
                field,
                input.organizer
            )
        )

        return createdFirstCome
    }

    fun approved(id: String): ApprovedFirstCome {
        val createdFirstCome = firstComeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found first come: $id") };

        return firstComeRepository.save(createdFirstCome.approve())
    }
}
