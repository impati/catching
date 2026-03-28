package org.example.impati.catching

import java.util.*

interface FirstComeRepository {

    fun save(firstCome: FirstCome): FirstCome

    fun findById(id: String): Optional<FirstCome>

    fun findAll(): List<FirstCome>
}
