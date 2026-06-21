package org.example.impati.catching.field

import org.springframework.stereotype.Component

sealed interface Field {
    val name: String
    val type: FieldType
}

data class NormalField(
    override val name: String,
    override val type: FieldType
) : Field

data class MobileField(
    override val name: String,
    override val type: FieldType = FieldType.MOBILE,
) : Field

data class DataSourceField(
    override val name: String,
    val dataSource: DataSource,
    override val type: FieldType = FieldType.DATA_SOURCE,
) : Field

data class SelectField(
    override val name: String,
    override val type: FieldType,
    val domain: List<String>,
) : Field

@Component
class FieldFactory {

    fun create(input: FieldInput): Field {
        return when (input.fieldType) {
            FieldType.NORMAL -> NormalField(input.name, input.fieldType)
            FieldType.MOBILE -> MobileField(input.name, input.fieldType)
            FieldType.DATA_SOURCE -> DataSourceField(
                input.name,
                input.dataSource!!,
                input.fieldType
            )

            FieldType.MULTIPLE_DOMAIN, FieldType.SINGLE_DOMAIN -> SelectField(
                input.name,
                input.fieldType,
                input.domain!!
            )
        }
    }
}
