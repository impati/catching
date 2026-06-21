package org.example.impati.catching.field

interface FieldRepository {

    fun save(field: Field): Field

    fun findAll(): List<Field>

    fun findByField(fieldName: String): Field
}
