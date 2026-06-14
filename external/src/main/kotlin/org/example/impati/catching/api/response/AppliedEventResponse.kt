package org.example.impati.catching.api.response

import org.example.impati.catching.applied_event.AppliedEvent

data class AppliedEventResponse(
    val firstComeId: String,
    val memberId: String
) {

    companion object {

        fun from(appliedEvent: AppliedEvent): AppliedEventResponse {
            return AppliedEventResponse(appliedEvent.firstCome.id, appliedEvent.member.id)
        }
    }
}
