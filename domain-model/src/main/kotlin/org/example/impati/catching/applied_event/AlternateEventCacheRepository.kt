package org.example.impati.catching.applied_event

import org.example.impati.catching.first_come.FirstCome

interface AlternateEventCacheRepository {

    fun add(firstCome: FirstCome): Int

    fun minus(firstCome: FirstCome): Int
}
