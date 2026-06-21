package org.example.impati.catching.api.response

import org.example.impati.catching.first_come.ApprovedFirstCome
import org.example.impati.catching.first_come.CreatedFirstCome

data class FirstComeResponse(
    val id: String,
    val name: String,
    val capacity: Int,
    val status: String,
    val time: FirstComeTimeResponse,
    val eligibility: EligibilityResponse,
    val join: JoinResponse,
    val waitPolicy: WaitPolicyResponse,
    val fields: List<FieldResponse>,
    val organizer: String,
) {

    companion object {

        fun from(createdFirstCome: CreatedFirstCome): FirstComeResponse =
            FirstComeResponse(
                id = createdFirstCome.id,
                name = createdFirstCome.name.value,
                capacity = createdFirstCome.capacity.value,
                status = "CREATED",
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
                fields = createdFirstCome.fields.map { FieldResponse.from(it) },
                organizer = createdFirstCome.organizer.value,
            )

        fun from(approvedFirstCome: ApprovedFirstCome): FirstComeResponse =
            FirstComeResponse(
                id = approvedFirstCome.id,
                name = approvedFirstCome.name.value,
                capacity = approvedFirstCome.capacity.value,
                status = "READY",
                time = FirstComeTimeResponse(
                    startAt = approvedFirstCome.time.startAt,
                    endAt = approvedFirstCome.time.endAt,
                    displayAt = approvedFirstCome.time.displayAt,
                ),
                eligibility = EligibilityResponse(
                    value = approvedFirstCome.eligibility.value,
                    duplicable = approvedFirstCome.eligibility.duplicable,
                ),
                join = JoinResponse(method = approvedFirstCome.join.method),
                waitPolicy = WaitPolicyResponse(
                    waitType = approvedFirstCome.waitPolicy.waitType,
                    capacity = approvedFirstCome.waitPolicy.capacity,
                ),
                fields = approvedFirstCome.fields.map { FieldResponse.from(it) },
                organizer = approvedFirstCome.organizer.value,
            )
    }
}
