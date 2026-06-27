package org.example.impati.catching.api.controller

import org.example.impati.catching.*
import org.example.impati.catching.api.request.InformationsRequest
import org.example.impati.catching.api.response.AppliedEventResponse
import org.example.impati.catching.api.response.FieldResponse
import org.example.impati.catching.applied_event.exception.NotFoundAppliedEvent
import org.example.impati.catching.field.Information
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@RestController
class AppliedForController(
    private val appliedEventCommand: AppliedEventCommand,
    private val appliedMemberCommand: AppliedMemberCommand,
    private val appliedEventQuery: AppliedEventQuery,
    private val firstComeQuery: FirstComeQuery,
    private val memberQuery: MemberQuery
) {

    /**
     * 선착순 신청 결과
     */
    @GetMapping("/v1/comes/{comeId}/apply-for")
    fun getApplyFor(
        @PathVariable comeId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String
    ): AppliedEventResponse {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        val appliedEvent = appliedEventQuery.findAppliedEvent(firstCome, member)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Applied event not found")

        return AppliedEventResponse.from(appliedEvent);
    }

    /**
     * 선착순 신청 - 1단계, 신청
     */
    @PostMapping("/v1/comes/{comeId}/apply-for")
    fun applyFor(
        @PathVariable comeId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String
    ) {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        appliedEventCommand.applyFor(firstCome, member)
    }

    /**
     * 선착순 신청 - 2단계, 정보 입력을 위한 필드 조회
     */
    @GetMapping("/v1/comes/{comeId}/fields")
    fun getFields(
        @PathVariable comeId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String
    ): List<FieldResponse> {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        if (appliedEventQuery.findAppliedEvent(firstCome, member) == null) {
            throw NotFoundAppliedEvent()
        }

        return firstCome.fields.map { FieldResponse.from(it) }
    }

    /**
     * 선착순 신청 - 2단계, 정보 입력
     */
    @PostMapping("/v1/comes/{comeId}/information")
    fun information(
        @PathVariable comeId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @RequestBody request: InformationsRequest
    ) {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        if (appliedEventQuery.findAppliedEvent(firstCome, member) == null) {
            throw NotFoundAppliedEvent()
        }

        appliedMemberCommand.create(
            firstCome,
            member,
            request.informations.map { Information(it.name, it.values) }
        )
    }
}
