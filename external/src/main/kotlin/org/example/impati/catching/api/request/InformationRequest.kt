package org.example.impati.catching.api.request

data class InformationsRequest(
    val informations: List<InformationRequest>
) {

    data class InformationRequest(
        val name: String,
        val values: List<String>
    )
}
