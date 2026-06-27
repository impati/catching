package org.example.impati.catching

import org.example.impati.catching.field.Datasource
import org.example.impati.catching.field.DatasourceRepository
import org.example.impati.catching.field.exception.NotFoundDataSourceException
import org.springframework.stereotype.Component

@Component
class DatasourceQuery(
    private val repository: DatasourceRepository
) {

    fun getDatasource(name: String): Datasource {
        return repository.findById(name).orElseThrow { NotFoundDataSourceException("Datasource $name not found") }
    }

    fun findAll(): List<Datasource> {
        return repository.findAll()
    }
}
