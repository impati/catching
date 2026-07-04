package org.example.impati.catching.executor

import org.example.impati.catching.MemberAgreementCommand
import org.example.impati.catching.MemberAgreementQuery
import org.example.impati.catching.auth.Member
import org.example.impati.catching.member_agreement.Agreement
import org.example.impati.catching.member_agreement.MemberAgreementByTerms
import org.example.impati.catching.terms.TermsGroup
import org.springframework.stereotype.Component

@Component
class MemberAgreementExecutor(
    private val memberAgreementCommand: MemberAgreementCommand,
    private val memberAgreementQuery: MemberAgreementQuery
) {

    fun agreeAndDisAgreeBy(
        member: Member,
        agreements: List<Agreement>,
        termsGroup: TermsGroup
    ): MemberAgreementByTerms {
        val memberAgreement = memberAgreementCommand.agreeAndDisagree(member, agreements)
        memberAgreement.validateRequiredTerms(termsGroup)

        return memberAgreementQuery.getMemberAgreementBy(member, termsGroup)
    }
}
