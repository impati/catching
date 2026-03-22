package org.example.impati.catching

data class FirstComeInputVo(
    val name: FirstComeName,
    val capacity: FirstComeCapacity,
    val time: FirstComeTime,
    val eligibility: Eligibility = Eligibility.basic(),
    val join: Join = Join(JoinMethod.IMMEDIATELY),
    val waitPolicy: WaitPolicy = WaitPolicy.waitlist(),
    val organizer: Organizer,
) {
}
