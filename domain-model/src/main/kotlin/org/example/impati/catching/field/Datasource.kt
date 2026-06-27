package org.example.impati.catching.field

import java.time.LocalDateTime

class Datasource(
    val name: String,
    val url: String,
    val createdBy: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
}
