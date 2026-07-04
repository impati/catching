package org.example.impati.catching.api.controller

import org.example.impati.catching.*
import org.example.impati.catching.api.request.AgreementRequests
import org.example.impati.catching.api.request.InformationsRequest
import org.example.impati.catching.api.response.*
import org.example.impati.catching.applied_event.exception.NotFoundAppliedEvent
import org.example.impati.catching.applied_member.exception.NotFoundAppliedMemberException
import org.example.impati.catching.field.Information
import org.example.impati.catching.terms.TermsGroupType
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@RestController
class AppliedForController(
    private val appliedEventCommand: AppliedEventCommand,
    private val appliedMemberCommand: AppliedMemberCommand,
    private val appliedMemberQuery: AppliedMemberQuery,
    private val appliedEventQuery: AppliedEventQuery,
    private val firstComeQuery: FirstComeQuery,
    private val memberQuery: MemberQuery,
    private val termsQuery: TermsQuery,
    private val memberAgreementCommand: MemberAgreementCommand,
    private val memberAgreementQuery: MemberAgreementQuery,
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
     * 선착순 신청 - 0단계, 신청 약관 노출
     */
    @GetMapping("/v1/terms-groups/APPLY_FOR")
    fun displayApplyForTerms(): TermsGroupResponse {
        return TermsGroupResponse.from(termsQuery.getTermsGroup(TermsGroupType.APPLY_FOR));
    }

    /**
     * 선착순 신청 - 0단계, 약관 동의 상태
     */
    @GetMapping("/v1/terms-groups/APPLY_FOR/agreement")
    fun agreementStatus(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String
    ): TermsAgreementResponses {
        val member = memberQuery.getMember(authorization)
        val termsGroup = termsQuery.getTermsGroup(TermsGroupType.APPLY_FOR)
        val memberAgreementByTerms = memberAgreementQuery.getMemberAgreementBy(member, termsGroup)

        return TermsAgreementResponses.from(memberAgreementByTerms)
    }

    /**
     * 선착순 신청 - 0단계, 약관 동의
     */
    @PostMapping("/v1/terms-groups/APPLY_FOR/agreement")
    fun agreement(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @RequestBody request: AgreementRequests
    ): TermsAgreementResponses {
        val member = memberQuery.getMember(authorization)
        val termsGroup = termsQuery.getTermsGroup(TermsGroupType.APPLY_FOR)
        request.agreements.forEach {
            if (!termsGroup.contain(it.termsId)) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "matched fail terms")
            }
        }

        val agree = termsQuery.getTerms(request.agreements.filter { it.agree }.map { it.termsId }.toList())
        memberAgreementCommand.agree(member, agree)

        val disagree = termsQuery.getTerms(request.agreements.filter { !it.agree }.map { it.termsId }.toList())
        memberAgreementCommand.disagree(member, disagree)

        val memberAgreementBy = memberAgreementQuery.getMemberAgreementBy(member, termsGroup)
        return TermsAgreementResponses.from(memberAgreementBy)
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
        if (appliedEventQuery.notExist(firstCome, member)) {
            throw NotFoundAppliedEvent()
        }

        return firstCome.fields.map { FieldResponse.from(it) }
    }

    /**
     * 선착순 신청 - 2단계, 정보 입력 결과
     */
    @GetMapping("/v1/comes/{comeId}/information")
    fun getInformation(
        @PathVariable comeId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String
    ): AppliedMemberResponse {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        if (appliedEventQuery.notExist(firstCome, member)) {
            throw NotFoundAppliedEvent()
        }
        if (appliedMemberQuery.notExists(firstCome, member)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Information not found")
        }

        return AppliedMemberResponse.from(appliedMemberQuery.getAppliedMember(firstCome, member))
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
        if (appliedEventQuery.notExist(firstCome, member)) {
            throw NotFoundAppliedEvent()
        }
        if (appliedMemberQuery.exists(firstCome, member)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "already exists")
        }

        appliedMemberCommand.create(
            firstCome,
            member,
            request.informations.map { Information(it.name, it.values) }
        )
    }

    /**
     * 선착순 신청 - 2단계, 정보 수정
     */
    @PutMapping("/v1/comes/{comeId}/information")
    fun updateInformation(
        @PathVariable comeId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @RequestBody request: InformationsRequest
    ) {
        val member = memberQuery.getMember(authorization);
        val firstCome = firstComeQuery.findById(comeId, LocalDateTime.now())
        if (appliedEventQuery.notExist(firstCome, member)) {
            throw NotFoundAppliedEvent()
        }
        if (appliedMemberQuery.notExists(firstCome, member)) {
            throw NotFoundAppliedMemberException()
        }

        appliedMemberCommand.edit(
            firstCome,
            member,
            request.informations.map { Information(it.name, it.values) }
        )
    }
}
