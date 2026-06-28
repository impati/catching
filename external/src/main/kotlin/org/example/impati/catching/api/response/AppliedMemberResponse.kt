package org.example.impati.catching.api.response

import org.example.impati.catching.applied_member.AppliedMember

data class AppliedMemberResponse(
    val firstComeId: String,
    val memberId: String,
    val informations: List<InformationResponse>
) {

    data class InformationResponse(
        val name: String,
        val values: List<String>
    )

    companion object {

        fun from(appliedMember: AppliedMember): AppliedMemberResponse {
            return AppliedMemberResponse(
                firstComeId = appliedMember.comeId,
                memberId = appliedMember.memberId,
                informations = appliedMember.informations.map {
                    InformationResponse(
                        name = it.name,
                        values = it.values
                    )
                }
            )
        }
    }
}
