package org.example.impati.catching

import org.springframework.stereotype.Component
import java.util.*

@Component
class FirstComeCommand(
    val firstComeRepository: FirstComeRepository
) {

    fun create(input: FirstComeInputVo): FirstCome {
        val id = UUID.randomUUID().toString().substring(0, 7);

        val firstCome = firstComeRepository.save(
            FirstCome(
                id,
                input.name,
                input.capacity,
                FirstComeStatus.PENDING,
                input.time,
                input.eligibility,
                input.join,
                input.waitPolicy,
                input.organizer
            )
        )

        return firstCome
    }
}
