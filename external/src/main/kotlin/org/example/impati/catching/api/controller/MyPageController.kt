package org.example.impati.catching.api.controller

import org.example.impati.catching.AppliedEventQuery
import org.example.impati.catching.api.response.MyPageResponse
import org.example.impati.catching.applied_member.AppliedMemberRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyPageController(
    private val appliedMemberRepository: AppliedMemberRepository,
    private val appliedEventQuery: AppliedEventQuery
) {

    /**
     * 내가 신청한 이벤트 조회
     * - 과거부터 현재까지
     */
    @GetMapping("/v1/my-page")
    fun myPage(): MyPageResponse {
        TODO()
    }
}
