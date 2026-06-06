package org.example.impati.catching.api.controller

import org.example.impati.catching.AppliedEventCommand
import org.example.impati.catching.FirstComeQuery
import org.example.impati.catching.MemberQuery
import org.example.impati.catching.api.response.FirstComeResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class ExternalFirstComeController(
    private val appliedEventCommand: AppliedEventCommand,
    private val firstComeQuery: FirstComeQuery,
    private val memberQuery: MemberQuery
) {

    @GetMapping("/v1/comes")
    fun comes(): List<FirstComeResponse> {
        return firstComeQuery.findByActive(LocalDateTime.now())
            .map { FirstComeResponse.from(it) }
    }

    @GetMapping("/v1/comes/{comeId}")
    fun comesDetail(@PathVariable comeId: String): FirstComeResponse {
        return FirstComeResponse.from(firstComeQuery.findById(comeId, LocalDateTime.now()))
    }

    @PostMapping("/v1/comes/{comeId}")
    fun applyFor(@PathVariable comeId: String, @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String) {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        appliedEventCommand.applyFor(firstCome, member)
        TODO();
    }
}
