package org.example.impati.catching.applied_event

import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstCome

class AlternateEvent(
    val firstCome: FirstCome,
    val member: Member
) {
}
