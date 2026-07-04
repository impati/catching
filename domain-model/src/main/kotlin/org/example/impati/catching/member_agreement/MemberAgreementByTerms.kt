package org.example.impati.catching.member_agreement

import org.example.impati.catching.terms.TermsGroupType

data class MemberAgreementByTerms(
    val memberId: String,
    val termsGroupType: TermsGroupType,
    val agreements: List<Agreement>
) {
}
