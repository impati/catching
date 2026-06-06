package org.example.impati.catching.applied_event

import org.example.impati.catching.first_come.FirstCome

interface AlternateEventRepository {

    fun save(alternateEvent: AlternateEvent)

    fun count(firstCome: FirstCome): Int
}
