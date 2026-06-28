package org.example.impati.catching.api.controller

import org.example.impati.catching.TermsCommand
import org.example.impati.catching.TermsQuery
import org.example.impati.catching.api.request.TermsGroupRequest
import org.example.impati.catching.api.request.TermsRequest
import org.example.impati.catching.api.response.TermsGroupResponse
import org.example.impati.catching.api.response.TermsResponse
import org.example.impati.catching.terms.TermsGroupType
import org.springframework.web.bind.annotation.*

@RestController
class TermsController(
    private val termsCommand: TermsCommand,
    private val termsQuery: TermsQuery,
) {

    /**
     * 약관 생성
     */
    @PostMapping("/v1/terms")
    fun createTerms(@RequestBody request: TermsRequest): TermsResponse {
        return TermsResponse.from(termsCommand.create(request.title, request.content))
    }

    /**
     * 모든 약관 조회
     */
    @GetMapping("/v1/terms")
    fun getTerms(): List<TermsResponse> {
        return termsQuery.allTerms().map { TermsResponse.from(it) }
    }

    /**
     * 약관 그룹과 매핑
     */
    @PutMapping("/v1/terms-groups/{termsGroupType}")
    fun updateTermsGroup(
        @PathVariable termsGroupType: TermsGroupType,
        @RequestBody request: TermsGroupRequest
    ): TermsGroupResponse {
        return TermsGroupResponse.from(termsCommand.updateTermsGroup(termsGroupType, request.termsIds))
    }
}
