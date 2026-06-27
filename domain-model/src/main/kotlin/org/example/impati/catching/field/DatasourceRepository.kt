package org.example.impati.catching.field

import java.util.*

interface DatasourceRepository {

    fun save(dataSource: Datasource): Datasource

    fun findById(name: String): Optional<Datasource>

    fun findAll(): List<Datasource>
}
