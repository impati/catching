package org.example.impati.catching.api.controller

import org.example.impati.catching.AppliedEventQuery
import org.example.impati.catching.FirstComeQuery
import org.example.impati.catching.api.response.FirstComeResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class ExternalFirstComeController(
    private val firstComeQuery: FirstComeQuery,
    private val appliedEventQuery: AppliedEventQuery
) {

    @GetMapping("/v1/comes")
    fun comes(): List<FirstComeResponse> {
        return firstComeQuery.findByActive(LocalDateTime.now())
            .map { FirstComeResponse.of(it, appliedEventQuery.countAppliedEvents(it)) }
    }

    @GetMapping("/v1/comes/{comeId}/active")
    fun comesDetail(@PathVariable comeId: String): FirstComeResponse {
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        val appliedEventNumber = appliedEventQuery.countAppliedEvents(firstCome)

        return FirstComeResponse.of(firstCome, appliedEventNumber)
    }
}
