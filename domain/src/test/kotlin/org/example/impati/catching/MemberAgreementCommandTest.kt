package org.example.impati.catching

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.example.impati.catching.auth.Member
import org.example.impati.catching.member_agreement.Agreement
import org.example.impati.catching.member_agreement.MemberAgreement
import org.example.impati.catching.member_agreement.MemberAgreementRepository
import org.example.impati.catching.member_agreement.exception.RequiredAgreementException
import org.example.impati.catching.terms.Terms
import org.example.impati.catching.terms.TermsGroup
import org.example.impati.catching.terms.TermsGroupType
import org.example.impati.catching.terms.TermsInGroup
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
        val termsGroup = TermsGroup.create(
            TermsGroupType.APPLY_FOR,
            listOf(TermsInGroup(requiredTerms, true), TermsInGroup(optionalTerms, false))
        )

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
        val termsGroup = TermsGroup.create(TermsGroupType.APPLY_FOR, listOf(TermsInGroup(terms, true)))

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
        val termsGroup = TermsGroup.create(
            TermsGroupType.APPLY_FOR,
            listOf(TermsInGroup(requiredTerms, true), TermsInGroup(optionalTerms, false))
        )

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

    @Test
    fun `필수 약관 동의가 누락되면 예외가 발생한다`() {
        // given
        val memberAgreement = MemberAgreement.empty("member-required-missing")
        val requiredTerms = Terms("missing-required-terms", "필수 약관", "필수 약관 내용")
        val optionalTerms = Terms("missing-optional-terms", "선택 약관", "선택 약관 내용")
        val termsGroup = TermsGroup.create(
            TermsGroupType.APPLY_FOR,
            listOf(TermsInGroup(requiredTerms, true), TermsInGroup(optionalTerms, false))
        )
        memberAgreement.addAgreement(Agreement(optionalTerms.id, true))

        // when & then
        assertThatThrownBy { memberAgreement.validateRequiredTerms(termsGroup) }
            .isInstanceOf(RequiredAgreementException::class.java)
    }

    @Test
    fun `필수 약관을 거부하면 예외가 발생한다`() {
        // given
        val memberAgreement = MemberAgreement.empty("member-required-false")
        val requiredTerms = Terms("false-required-terms", "필수 약관", "필수 약관 내용")
        val termsGroup = TermsGroup.create(
            TermsGroupType.APPLY_FOR,
            listOf(TermsInGroup(requiredTerms, true))
        )
        memberAgreement.addAgreement(Agreement(requiredTerms.id, false))

        // when & then
        assertThatThrownBy { memberAgreement.validateRequiredTerms(termsGroup) }
            .isInstanceOf(RequiredAgreementException::class.java)
    }

    @Test
    fun `선택 약관은 동의하지 않아도 필수 약관 검증을 통과한다`() {
        // given
        val memberAgreement = MemberAgreement.empty("member-optional-false")
        val requiredTerms = Terms("optional-pass-required-terms", "필수 약관", "필수 약관 내용")
        val optionalTerms = Terms("optional-pass-optional-terms", "선택 약관", "선택 약관 내용")
        val termsGroup = TermsGroup.create(
            TermsGroupType.APPLY_FOR,
            listOf(TermsInGroup(requiredTerms, true), TermsInGroup(optionalTerms, false))
        )
        memberAgreement.addAgreement(Agreement(requiredTerms.id, true))
        memberAgreement.addAgreement(Agreement(optionalTerms.id, false))

        // when & then
        assertThatCode { memberAgreement.validateRequiredTerms(termsGroup) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `필수 약관 검증에 실패하면 동의 상태를 저장하지 않는다`() {
        // given
        val member = Member("tester", "member-agreement-required-fail")
        val requiredTerms = Terms("save-fail-required-terms", "필수 약관", "필수 약관 내용")
        val termsGroup = TermsGroup.create(
            TermsGroupType.APPLY_FOR,
            listOf(TermsInGroup(requiredTerms, true))
        )

        // when & then
        assertThatThrownBy {
            sut.agreeAndDisagree(member, listOf(Agreement(requiredTerms.id, false)), termsGroup)
        }.isInstanceOf(RequiredAgreementException::class.java)

        assertThat(memberAgreementRepository.findBy(member).agreements).isEmpty()
    }
}
