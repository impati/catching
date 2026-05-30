package org.example.impati.testsupport.fixture

import org.example.impati.catching.first_come.Eligibility
import org.example.impati.catching.first_come.FirstCome
import org.example.impati.catching.first_come.FirstComeCapacity
import org.example.impati.catching.first_come.FirstComeInputVo
import org.example.impati.catching.first_come.FirstComeName
import org.example.impati.catching.first_come.FirstComeStatus
import org.example.impati.catching.first_come.FirstComeTime
import org.example.impati.catching.first_come.Join
import org.example.impati.catching.first_come.JoinMethod
import org.example.impati.catching.first_come.Organizer
import org.example.impati.catching.first_come.WaitPolicy
import java.time.LocalDateTime

fun firstComeInput(block: FirstComeInputBuilder.() -> Unit = {}): FirstComeInputVo {
    return FirstComeInputBuilder().apply(block).build()
}

fun firstCome(block: FirstComeBuilder.() -> Unit = {}): FirstCome {
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
    var organizer: String = "impati"

    fun build(): FirstComeInputVo {
        return FirstComeInputVo(
            name = FirstComeName(name),
            capacity = FirstComeCapacity(capacity),
            time = time,
            eligibility = eligibility,
            join = join,
            waitPolicy = waitPolicy,
            organizer = Organizer(organizer)
        )
    }
}

class FirstComeBuilder {

    var id: String = "first01"
    var name: String = "open sale"
    var capacity: Int = 100
    var status: FirstComeStatus = FirstComeStatus.PENDING
    var time: FirstComeTime = firstComeTime()
    var eligibility: Eligibility = Eligibility.basic()
    var join: Join = Join(JoinMethod.IMMEDIATELY)
    var waitPolicy: WaitPolicy = WaitPolicy.waitlist()
    var organizer: String = "impati"

    fun build(): FirstCome {
        return FirstCome(
            id = id,
            name = FirstComeName(name),
            capacity = FirstComeCapacity(capacity),
            status = status,
            time = time,
            eligibility = eligibility,
            join = join,
            waitPolicy = waitPolicy,
            organizer = Organizer(organizer)
        )
    }
}
