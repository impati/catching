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
    val appliedEventNumber: Int,
    val organizer: String,
) {

    companion object {

        fun of(firstCome: ActiveFirstCome, appliedEventNumber: Int): FirstComeResponse =
            FirstComeResponse(
                id = firstCome.id,
                name = firstCome.name.value,
                capacity = firstCome.capacity.value,
                status = "ACTIVE",
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
                appliedEventNumber = appliedEventNumber,
                organizer = firstCome.organizer.value,
            )
    }
}
