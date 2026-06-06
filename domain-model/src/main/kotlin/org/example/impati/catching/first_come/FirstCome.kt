package org.example.impati.catching.first_come

import java.time.LocalDateTime

sealed interface FirstCome {
    val id: String
    val name: FirstComeName
    val capacity: FirstComeCapacity
    val time: FirstComeTime
    val eligibility: Eligibility
    val join: Join
    val waitPolicy: WaitPolicy
    val organizer: Organizer
}

data class CreatedFirstCome(
    override val id: String,
    override val name: FirstComeName,
    override val capacity: FirstComeCapacity,
    override val time: FirstComeTime,
    override val eligibility: Eligibility,
    override val join: Join,
    override val waitPolicy: WaitPolicy,
    override val organizer: Organizer
) : FirstCome {

    fun approve(): ApprovedFirstCome {
        return ApprovedFirstCome(
            id = id,
            name = name,
            capacity = capacity,
            time = time,
            eligibility = eligibility,
            join = join,
            waitPolicy = waitPolicy,
            organizer = organizer
        )
    }
}

data class ApprovedFirstCome(
    override val id: String,
    override val name: FirstComeName,
    override val capacity: FirstComeCapacity,
    override val time: FirstComeTime,
    override val eligibility: Eligibility,
    override val join: Join,
    override val waitPolicy: WaitPolicy,
    override val organizer: Organizer
) : FirstCome {

    fun toActive(now: LocalDateTime): ActiveFirstCome {
        return ActiveFirstCome(
            id = id,
            name = name,
            capacity = capacity,
            time = time,
            eligibility = eligibility,
            join = join,
            waitPolicy = waitPolicy,
            organizer = organizer,
            now = now
        )
    }
}

data class ActiveFirstCome(
    override val id: String,
    override val name: FirstComeName,
    override val capacity: FirstComeCapacity,
    override val time: FirstComeTime,
    override val eligibility: Eligibility,
    override val join: Join,
    override val waitPolicy: WaitPolicy,
    override val organizer: Organizer,
    val now: LocalDateTime
) : FirstCome {

    init {
        val displayAt = time.displayAt
        val endAt = time.endAt
        require(displayAt <= now) { "displayAt must be greater than now" }
        require(now <= endAt) { "now must be less than endAt" }
    }

}
