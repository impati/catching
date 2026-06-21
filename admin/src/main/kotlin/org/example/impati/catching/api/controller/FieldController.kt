package org.example.impati.catching.api.controller

import org.example.impati.catching.DataSourceQuery
import org.example.impati.catching.FieldCommand
import org.example.impati.catching.FieldQuery
import org.example.impati.catching.api.request.FieldRequest
import org.example.impati.catching.api.response.FieldResponse
import org.example.impati.catching.field.FieldInput
import org.springframework.web.bind.annotation.*

@RestController
class FieldController(
    private val fieldCommand: FieldCommand,
    private val fieldQuery: FieldQuery,
    private val dataSourceQuery: DataSourceQuery
) {

    @PostMapping("/v1/fields")
    fun createField(@RequestBody request: FieldRequest): FieldResponse {
        val dataSource = request.dataSource?.let {
            dataSourceQuery.getDataSource(it)
        }

        val input = FieldInput(
            name = request.name,
            fieldType = request.fieldType,
            dataSource = dataSource,
            domain = request.domain,
        )

        return FieldResponse.from(fieldCommand.create(input));
    }

    @GetMapping("/v1/fields")
    fun getFields(): List<FieldResponse> {
        return fieldQuery.allFields().map { FieldResponse.from(it) }
    }

    @GetMapping("/v1/fields/{name}")
    fun getField(@PathVariable name: String): FieldResponse {
        return FieldResponse.from(fieldQuery.getField(name))
    }
}
