package org.example.impati.catching

import org.example.impati.catching.field.Datasource
import org.example.impati.catching.field.DatasourceInput
import org.example.impati.catching.field.DatasourceRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DatasourceCommand(
    private val datasourceRepository: DatasourceRepository,
) {

    fun create(input: DatasourceInput): Datasource {
        val dataSource = Datasource(input.name, input.url, input.createdBy, LocalDateTime.now())
        return datasourceRepository.save(dataSource)
    }
}
