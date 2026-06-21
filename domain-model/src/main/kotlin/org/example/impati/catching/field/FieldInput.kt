package org.example.impati.catching.field

data class FieldInput(
    val name: String,
    val fieldType: FieldType,
    val dataSource: DataSource?,
    val domain: List<String>?
)
