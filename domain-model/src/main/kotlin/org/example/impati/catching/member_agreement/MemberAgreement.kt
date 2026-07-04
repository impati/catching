package org.example.impati.catching.member_agreement

import org.example.impati.catching.member_agreement.exception.RequiredAgreementException
import org.example.impati.catching.terms.TermsGroup

class MemberAgreement(
    val memberId: String,
    val agreements: MutableList<Agreement>
) {

    fun validateRequiredTerms(termsGroup: TermsGroup) {
        termsGroup.values.forEach {
            if (it.required && get(it.terms.id)?.value != true) {
                throw RequiredAgreementException()
            }
        }
    }

    fun addAgreement(agreement: Agreement) {
        if (contains(agreement.termsId)) {
            agreements.remove(get(agreement.termsId))
        }

        agreements.add(agreement)
    }

    fun get(termsId: String): Agreement? {
        if (contains(termsId)) {
            return agreements.find { it.termsId == termsId }
        }

        return null
    }

    fun contains(termsId: String): Boolean {
        return agreements.map { it.termsId }.contains(termsId)
    }

    companion object {

        fun empty(memberId: String): MemberAgreement {
            return MemberAgreement(
                memberId,
                mutableListOf()
            )
        }
    }
}
