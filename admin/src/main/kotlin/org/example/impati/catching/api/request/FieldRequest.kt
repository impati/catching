package org.example.impati.catching.api.request

import org.example.impati.catching.field.FieldType

data class FieldRequest(
    val name: String,
    val fieldType: FieldType,
    val dataSource: String?,
    val domain: List<String>?,
    val required: Boolean
)
