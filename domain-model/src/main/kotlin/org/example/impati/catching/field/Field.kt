package org.example.impati.catching.field

import org.springframework.stereotype.Component

sealed interface Field {
    val name: String
    val type: FieldType
    val required: Boolean
}

data class NormalField(
    override val name: String,
    override val type: FieldType,
    override val required: Boolean = false,
) : Field

data class MobileField(
    override val name: String,
    override val type: FieldType = FieldType.MOBILE,
    override val required: Boolean = false,
) : Field

data class DataSourceField(
    override val name: String,
    val dataSource: Datasource,
    override val type: FieldType = FieldType.DATA_SOURCE,
    override val required: Boolean = false,
) : Field

data class SelectField(
    override val name: String,
    override val type: FieldType,
    val domain: List<String>,
    override val required: Boolean = false,
) : Field

@Component
class FieldFactory {

    fun create(input: FieldInput): Field {
        return when (input.fieldType) {
            FieldType.NORMAL -> NormalField(input.name, input.fieldType, input.required)
            FieldType.MOBILE -> MobileField(input.name, input.fieldType, input.required)
            FieldType.DATA_SOURCE -> DataSourceField(
                input.name,
                input.dataSource!!,
                input.fieldType,
                input.required
            )

            FieldType.MULTIPLE_DOMAIN, FieldType.SINGLE_DOMAIN -> SelectField(
                input.name,
                input.fieldType,
                input.domain!!,
                input.required
            )
        }
    }
}
