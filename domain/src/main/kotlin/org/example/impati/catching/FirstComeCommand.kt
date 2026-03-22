package org.example.impati.catching

import java.util.*

class FirstComeCommand {

    fun create(input: FirstComeInputVo): FirstCome {
        val id = UUID.randomUUID().toString().substring(0, 7);

        return FirstCome(
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
    }
}
