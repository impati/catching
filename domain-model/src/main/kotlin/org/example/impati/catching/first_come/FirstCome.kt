package org.example.impati.catching.first_come

class FirstCome(
    val id: String,
    val name: FirstComeName,
    val capacity: FirstComeCapacity,
    val status: FirstComeStatus,
    val time: FirstComeTime,
    val eligibility: Eligibility,
    val join: Join,
    val waitPolicy: WaitPolicy,
    val organizer: Organizer
) {
}
