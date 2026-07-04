package org.example.impati.catching.application

import org.example.impati.catching.DatasourceCommand
import org.example.impati.catching.DatasourceQuery
import org.example.impati.catching.FieldCommand
import org.example.impati.catching.FieldQuery
import org.example.impati.catching.api.request.FieldRequest
import org.example.impati.catching.api.response.FieldResponse
import org.example.impati.catching.field.FieldInput
import org.springframework.stereotype.Component

@Component
class FieldService(
    private val fieldCommand: FieldCommand,
    private val fieldQuery: FieldQuery,
    private val datasourceCommand: DatasourceCommand,
    private val datasourceQuery: DatasourceQuery
) {

    fun createField(request: FieldRequest): FieldResponse {
        val dataSource = request.dataSource?.let {
            datasourceQuery.getDatasource(it)
        }

        val input = FieldInput(
            name = request.name,
            fieldType = request.fieldType,
            dataSource = dataSource,
            domain = request.domain,
            required = request.required
        )

        return FieldResponse.from(fieldCommand.create(input));
    }
}
