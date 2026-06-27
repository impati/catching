package org.example.impati.catching.field

data class FieldInput(
    val name: String,
    val fieldType: FieldType,
    val dataSource: Datasource?,
    val domain: List<String>?,
    val required: Boolean
)
