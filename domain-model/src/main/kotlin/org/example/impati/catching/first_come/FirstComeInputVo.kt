package org.example.impati.catching.first_come

import org.example.impati.catching.field.Field

data class FirstComeInputVo(
    val name: FirstComeName,
    val capacity: FirstComeCapacity,
    val time: FirstComeTime,
    val eligibility: Eligibility = Eligibility.basic(),
    val join: Join = Join(JoinMethod.IMMEDIATELY),
    val waitPolicy: WaitPolicy = WaitPolicy.waitlist(),
    val fields: List<Field> = emptyList(),
    val organizer: Organizer,
) {
}
