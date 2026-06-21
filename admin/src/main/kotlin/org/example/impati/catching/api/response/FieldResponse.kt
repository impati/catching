package org.example.impati.catching.api.response

import org.example.impati.catching.field.*

data class FieldResponse(
    val name: String,
    val fieldType: FieldType,
    val dataSource: DataSource? = null,
    val domain: List<String>? = listOf()
) {

    companion object {

        fun from(field: Field): FieldResponse {
            val dataSource = if (field is DataSourceField) field.dataSource else null
            val domain = if (field is SelectField) field.domain else null

            return FieldResponse(
                name = field.name,
                fieldType = field.type,
                dataSource = dataSource,
                domain = domain
            )
        }
    }
}
