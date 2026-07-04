package org.example.impati.catching.api.response

import org.example.impati.catching.member_agreement.MemberAgreementByTerms

data class TermsAgreementResponses(
    val termsAgreements: List<TermsAgreementResponse>
) {

    companion object {

        fun from(memberAgreements: MemberAgreementByTerms): TermsAgreementResponses {
            return TermsAgreementResponses(
                memberAgreements.agreements.map { TermsAgreementResponse(it.termsId, it.value) }
            )
        }
    }


    data class TermsAgreementResponse(
        val termsId: String,
        val agree: Boolean
    ) {
    }
}
