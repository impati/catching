package org.example.impati.catching.api.controller

import org.example.impati.catching.DatasourceCommand
import org.example.impati.catching.DatasourceQuery
import org.example.impati.catching.FieldCommand
import org.example.impati.catching.FieldQuery
import org.example.impati.catching.api.request.DatasourceRequest
import org.example.impati.catching.api.request.FieldRequest
import org.example.impati.catching.api.response.DatasourceResponse
import org.example.impati.catching.api.response.FieldResponse
import org.example.impati.catching.field.DatasourceInput
import org.example.impati.catching.field.FieldInput
import org.springframework.web.bind.annotation.*

@RestController
class FieldController(
    private val fieldCommand: FieldCommand,
    private val fieldQuery: FieldQuery,
    private val datasourceCommand: DatasourceCommand,
    private val datasourceQuery: DatasourceQuery
) {

    @PostMapping("/v1/fields")
    fun createField(@RequestBody request: FieldRequest): FieldResponse {
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

    @GetMapping("/v1/fields")
    fun getFields(): List<FieldResponse> {
        return fieldQuery.allFields().map { FieldResponse.from(it) }
    }

    @GetMapping("/v1/fields/{name}")
    fun getField(@PathVariable name: String): FieldResponse {
        return FieldResponse.from(fieldQuery.getField(name))
    }

    @PostMapping("/v1/datasource")
    fun createDatasource(@RequestBody request: DatasourceRequest): DatasourceResponse {
        val input = DatasourceInput(
            request.name,
            request.url,
            request.requestBy
        )

        val datasource = datasourceCommand.create(input)

        return DatasourceResponse.from(datasource)
    }

    @GetMapping("/v1/datasource/{name}")
    fun getDatasource(@PathVariable name: String): DatasourceResponse {
        return DatasourceResponse.from(datasourceQuery.getDatasource(name))
    }

    @GetMapping("/v1/datasource")
    fun getDatasourceList(): List<DatasourceResponse> {
        return datasourceQuery.findAll().map { DatasourceResponse.from(it) }
    }
}
