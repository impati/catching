package org.example.impati.catching.api.request

data class TermsGroupRequest(
    val terms: List<TermsInGroupRequest>
) {

    data class TermsInGroupRequest(
        val termsId: String,
        val required: Boolean
    ) {

    }
}
