package org.example.impati.catching.api.response

import org.example.impati.catching.first_come.ActiveFirstCome

data class FirstComeResponse(
    val id: String,
    val name: String,
    val capacity: Int,
    val status: String,
    val time: FirstComeTimeResponse,
    val eligibility: EligibilityResponse,
    val join: JoinResponse,
    val waitPolicy: WaitPolicyResponse,
    val organizer: String,
) {

    companion object {

        fun from(createdFirstCome: ActiveFirstCome): FirstComeResponse =
            FirstComeResponse(
                id = createdFirstCome.id,
                name = createdFirstCome.name.value,
                capacity = createdFirstCome.capacity.value,
                status = "ON_GOING",
                time = FirstComeTimeResponse(
                    startAt = createdFirstCome.time.startAt,
                    endAt = createdFirstCome.time.endAt,
                    displayAt = createdFirstCome.time.displayAt,
                ),
                eligibility = EligibilityResponse(
                    value = createdFirstCome.eligibility.value,
                    duplicable = createdFirstCome.eligibility.duplicable,
                ),
                join = JoinResponse(method = createdFirstCome.join.method),
                waitPolicy = WaitPolicyResponse(
                    waitType = createdFirstCome.waitPolicy.waitType,
                    capacity = createdFirstCome.waitPolicy.capacity,
                ),
                organizer = createdFirstCome.organizer.value,
            )
    }
}
