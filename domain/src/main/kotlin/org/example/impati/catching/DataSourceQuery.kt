package org.example.impati.catching

import org.example.impati.catching.field.DataSource
import org.example.impati.catching.field.DataSourceRepository
import org.example.impati.catching.field.exception.NotFoundDataSourceException
import org.springframework.stereotype.Component

@Component
class DataSourceQuery(
    private val repository: DataSourceRepository
) {

    fun getDataSource(name: String): DataSource {
        return repository.findById(name).orElseThrow { NotFoundDataSourceException("DataSource $name not found") }
    }
}
