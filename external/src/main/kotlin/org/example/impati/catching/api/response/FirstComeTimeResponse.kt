package org.example.impati.catching.api.response

import java.time.LocalDateTime

data class FirstComeTimeResponse(
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val displayAt: LocalDateTime,
)
