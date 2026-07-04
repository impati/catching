package org.example.impati.catching.api.request

data class AgreementRequests(
    val agreements: List<AgreementRequest>
) {

    data class AgreementRequest(
        val termsId: String,
        val agree: Boolean
    ) {
    }
}
