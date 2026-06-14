package org.example.impati.catching.api.controller

import org.example.impati.catching.AppliedEventCommand
import org.example.impati.catching.AppliedEventQuery
import org.example.impati.catching.FirstComeQuery
import org.example.impati.catching.MemberQuery
import org.example.impati.catching.api.response.AppliedEventResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class AppliedForController(
    private val appliedEventCommand: AppliedEventCommand,
    private val appliedEventQuery: AppliedEventQuery,
    private val firstComeQuery: FirstComeQuery,
    private val memberQuery: MemberQuery
) {

    @PostMapping("/v1/comes/{comeId}/apply-for")
    fun applyFor(
        @PathVariable comeId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String
    ) {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        appliedEventCommand.applyFor(firstCome, member)
    }

    @GetMapping("/v1/comes/{comeId}/apply-for")
    fun getApplyFor(
        @PathVariable comeId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String
    ): AppliedEventResponse {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())

        return AppliedEventResponse.from(appliedEventQuery.getAppliedEvent(firstCome, member));
    }
}
