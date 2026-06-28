package org.example.impati.catching.api.response

import org.example.impati.catching.terms.Terms

data class TermsResponse(
    val title: String,
    val content: String,
) {

    companion object {

        fun from(terms: Terms): TermsResponse {
            return TermsResponse(
                title = terms.title,
                content = terms.content
            )
        }
    }
}
