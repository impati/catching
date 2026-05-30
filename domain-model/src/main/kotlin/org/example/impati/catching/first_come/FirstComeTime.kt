package org.example.impati.catching.first_come

import java.time.LocalDateTime

data class FirstComeTime(
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val displayAt: LocalDateTime
) {

    init {
        require(startAt.isBefore(endAt)) { "startAt must be before endAt" }
        require(displayAt.isBefore(endAt)) { "displayAt must be before endAt" }
    }
}
