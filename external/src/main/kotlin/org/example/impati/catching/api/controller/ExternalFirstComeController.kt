package org.example.impati.catching.api.controller

import org.example.impati.catching.FirstComeQuery
import org.example.impati.catching.api.response.FirstComeResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class ExternalFirstComeController(
    private val firstComeQuery: FirstComeQuery
) {

    @GetMapping("/v1/comes")
    fun comes(): List<FirstComeResponse> {
        return firstComeQuery.findByActive(LocalDateTime.now())
            .map { FirstComeResponse.from(it) }
    }
}
