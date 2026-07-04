package org.example.impati.catching

import org.assertj.core.api.Assertions.assertThat
import org.example.impati.catching.auth.Member
import org.example.impati.catching.member_agreement.MemberAgreementRepository
import org.example.impati.catching.terms.Terms
import org.example.impati.catching.terms.TermsGroup
import org.example.impati.catching.terms.TermsGroupType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MemberAgreementCommandTest : IntegrationTest() {

    @Autowired
    lateinit var sut: MemberAgreementCommand

    @Autowired
    lateinit var query: MemberAgreementQuery

    @Autowired
    lateinit var memberAgreementRepository: MemberAgreementRepository

    @Test
    fun `약관 동의를 저장한다`() {
        // given
        val member = Member("tester", "member-agreement-save")
        val requiredTerms = Terms("save-required-terms", "필수 약관", "필수 약관 내용")
        val optionalTerms = Terms("save-optional-terms", "선택 약관", "선택 약관 내용")
        val termsGroup = TermsGroup.create(TermsGroupType.APPLY_FOR, listOf(requiredTerms, optionalTerms))

        // when
        sut.agree(member, listOf(requiredTerms))

        // then
        val result = query.getMemberAgreementBy(member, termsGroup)
        assertThat(result.agreements)
            .extracting("termsId", "value")
            .containsExactlyInAnyOrder(
                org.assertj.core.groups.Tuple.tuple(requiredTerms.id, true),
                org.assertj.core.groups.Tuple.tuple(optionalTerms.id, false),
            )
    }

    @Test
    fun `약관 동의를 철회하면 기존 값을 갱신한다`() {
        // given
        val member = Member("tester", "member-agreement-update")
        val terms = Terms("update-terms", "갱신 약관", "갱신 약관 내용")
        val termsGroup = TermsGroup.create(TermsGroupType.APPLY_FOR, listOf(terms))

        // when
        sut.agree(member, listOf(terms))
        sut.disagree(member, listOf(terms))

        // then
        val result = query.getMemberAgreementBy(member, termsGroup)
        assertThat(result.agreements)
            .extracting("termsId", "value")
            .containsExactly(org.assertj.core.groups.Tuple.tuple(terms.id, false))
        assertThat(memberAgreementRepository.findBy(member).agreements).hasSize(1)
    }

    @Test
    fun `약관 동의 이력이 없는 회원은 전체 약관을 미동의로 조회한다`() {
        // given
        val member = Member("tester", "member-agreement-empty")
        val requiredTerms = Terms("empty-required-terms", "필수 약관", "필수 약관 내용")
        val optionalTerms = Terms("empty-optional-terms", "선택 약관", "선택 약관 내용")
        val termsGroup = TermsGroup.create(TermsGroupType.APPLY_FOR, listOf(requiredTerms, optionalTerms))

        // when
        val result = query.getMemberAgreementBy(member, termsGroup)

        // then
        assertThat(result.agreements)
            .extracting("termsId", "value")
            .containsExactlyInAnyOrder(
                org.assertj.core.groups.Tuple.tuple(requiredTerms.id, false),
                org.assertj.core.groups.Tuple.tuple(optionalTerms.id, false),
            )
    }
}
