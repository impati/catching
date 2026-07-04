package org.example.impati.catching.api.response

import org.example.impati.catching.terms.Terms
import org.example.impati.catching.terms.TermsInGroup

data class TermsResponse(
    val id: String,
    val title: String,
    val content: String,
    val required: Boolean = false,
) {

    companion object {

        fun from(terms: Terms): TermsResponse {
            return TermsResponse(
                terms.id,
                title = terms.title,
                content = terms.content
            )
        }

        fun from(termsInGroup: TermsInGroup): TermsResponse {
            return TermsResponse(
                termsInGroup.terms.id,
                title = termsInGroup.terms.title,
                content = termsInGroup.terms.content,
                required = termsInGroup.required
            )
        }
    }
}
