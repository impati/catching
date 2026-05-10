package org.example.impati.catching.first_come

data class WaitPolicy(
    val waitType: WaitType,
    val capacity: Int? = 0
) {

    companion object {

        fun waitlist(capacity: Int): WaitPolicy {
            return WaitPolicy(WaitType.WAITLIST, capacity)
        }

        fun waitlist(): WaitPolicy {
            return WaitPolicy(WaitType.WAITLIST, Int.MAX_VALUE)
        }
    }
}
