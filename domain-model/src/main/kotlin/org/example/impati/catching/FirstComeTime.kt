package org.example.impati.catching

import java.time.LocalDateTime

data class FirstComeTime(
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val displayAt: LocalDateTime
) {
}
