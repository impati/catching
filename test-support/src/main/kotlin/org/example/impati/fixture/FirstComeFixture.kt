package org.example.impati.fixture

import org.example.impati.catching.first_come.*
import org.example.impati.catching.field.Field
import java.time.LocalDateTime

fun firstComeInput(block: FirstComeInputBuilder.() -> Unit = {}): FirstComeInputVo {
    return FirstComeInputBuilder().apply(block).build()
}

fun firstCome(block: FirstComeBuilder.() -> Unit = {}): CreatedFirstCome {
    return FirstComeBuilder().apply(block).build()
}

fun firstComeTime(
    displayAt: LocalDateTime = LocalDateTime.of(2026, 6, 1, 9, 0),
    startAt: LocalDateTime = LocalDateTime.of(2026, 6, 1, 10, 0),
    endAt: LocalDateTime = LocalDateTime.of(2026, 6, 1, 11, 0),
): FirstComeTime {
    return FirstComeTime(
        startAt = startAt,
        endAt = endAt,
        displayAt = displayAt
    )
}

class FirstComeInputBuilder {

    var name: String = "open sale"
    var capacity: Int = 100
    var time: FirstComeTime = firstComeTime()
    var eligibility: Eligibility = Eligibility.basic()
    var join: Join = Join(JoinMethod.IMMEDIATELY)
    var waitPolicy: WaitPolicy = WaitPolicy.waitlist()
    var fields: List<String> = emptyList()
    var organizer: String = "impati"

    fun build(): FirstComeInputVo {
        return FirstComeInputVo(
            name = FirstComeName(name),
            capacity = FirstComeCapacity(capacity),
            time = time,
            eligibility = eligibility,
            join = join,
            waitPolicy = waitPolicy,
            fields = fields,
            organizer = Organizer(organizer)
        )
    }
}

class FirstComeBuilder {

    var id: String = "first01"
    var name: String = "open sale"
    var capacity: Int = 100
    var time: FirstComeTime = firstComeTime()
    var eligibility: Eligibility = Eligibility.basic()
    var join: Join = Join(JoinMethod.IMMEDIATELY)
    var waitPolicy: WaitPolicy = WaitPolicy.waitlist()
    var fields: List<Field> = emptyList()
    var organizer: String = "impati"

    fun build(): CreatedFirstCome {
        return CreatedFirstCome(
            id = id,
            name = FirstComeName(name),
            capacity = FirstComeCapacity(capacity),
            time = time,
            eligibility = eligibility,
            join = join,
            waitPolicy = waitPolicy,
            fields = fields,
            organizer = Organizer(organizer)
        )
    }
}
