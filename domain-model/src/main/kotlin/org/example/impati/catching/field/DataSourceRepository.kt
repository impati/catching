package org.example.impati.catching.field

import java.util.*

interface DataSourceRepository {

    fun save(dataSource: DataSource): DataSource

    fun findById(name: String): Optional<DataSource>
}
