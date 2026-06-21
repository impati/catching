package org.example.impati.catching

import org.example.impati.catching.field.Field
import org.example.impati.catching.field.FieldRepository
import org.springframework.stereotype.Component

@Component
class FieldQuery(
    private val fieldRepository: FieldRepository
) {

    fun allFields(): List<Field> {
        return fieldRepository.findAll()
    }

    fun getField(fieldName: String): Field {
        return fieldRepository.findByField(fieldName)
    }
}
