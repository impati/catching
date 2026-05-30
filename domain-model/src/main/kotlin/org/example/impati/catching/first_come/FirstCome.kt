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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FirstCome

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
