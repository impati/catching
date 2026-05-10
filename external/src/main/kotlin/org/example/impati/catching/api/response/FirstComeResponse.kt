package org.example.impati.catching.api.response

import org.example.impati.catching.first_come.FirstCome
import org.example.impati.catching.first_come.FirstComeStatus

data class FirstComeResponse(
    val id: String,
    val name: String,
    val capacity: Int,
    val status: FirstComeStatus,
    val time: FirstComeTimeResponse,
    val eligibility: EligibilityResponse,
    val join: JoinResponse,
    val waitPolicy: WaitPolicyResponse,
    val organizer: String,
) {

    companion object {

        fun from(firstCome: FirstCome): FirstComeResponse =
            FirstComeResponse(
                id = firstCome.id,
                name = firstCome.name.value,
                capacity = firstCome.capacity.value,
                status = firstCome.status,
                time = FirstComeTimeResponse(
                    startAt = firstCome.time.startAt,
                    endAt = firstCome.time.endAt,
                    displayAt = firstCome.time.displayAt,
                ),
                eligibility = EligibilityResponse(
                    value = firstCome.eligibility.value,
                    duplicable = firstCome.eligibility.duplicable,
                ),
                join = JoinResponse(method = firstCome.join.method),
                waitPolicy = WaitPolicyResponse(
                    waitType = firstCome.waitPolicy.waitType,
                    capacity = firstCome.waitPolicy.capacity,
                ),
                organizer = firstCome.organizer.value,
            )
    }
}
