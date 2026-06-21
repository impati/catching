package org.example.impati.catching

import org.example.impati.catching.field.Field
import org.example.impati.catching.field.FieldFactory
import org.example.impati.catching.field.FieldInput
import org.example.impati.catching.field.FieldRepository
import org.springframework.stereotype.Component

@Component
class FieldCommand(
    private val fieldRepository: FieldRepository,
    private val fieldFactory: FieldFactory
) {

    fun create(input: FieldInput): Field {
        return fieldRepository.save(fieldFactory.create(input))
    }
}
