package org.example.impati.catching.api.response

import org.example.impati.catching.terms.TermsGroup
import org.example.impati.catching.terms.TermsGroupType

data class TermsGroupResponse(
    val termsGroupType: TermsGroupType,
    val terms: List<TermsResponse>
) {

    companion object {

        fun from(termsGroup: TermsGroup): TermsGroupResponse {
            return TermsGroupResponse(
                termsGroup.type,
                termsGroup.values.map { TermsResponse.from(it) }
            )
        }
    }
}
