package org.example.impati.catching.field

import jakarta.persistence.*

@Entity
@Table(name = "field")
class FieldEntity(

    @Id
    @Column(name = "name", nullable = false, updatable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false)
    val fieldType: FieldType,

    @Column(name = "required", nullable = false)
    val required: Boolean,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "datasource_name")
    val datasource: DatasourceEntity?,

    @Convert(converter = StringListConverter::class)
    @Column(name = "domain", nullable = false, columnDefinition = "TEXT")
    val domain: List<String> = emptyList(),
) {

    companion object {

        fun from(
            field: Field,
            datasource: DatasourceEntity? = null,
        ): FieldEntity {
            return when (field) {
                is DataSourceField -> FieldEntity(
                    name = field.name,
                    fieldType = field.type,
                    required = field.required,
                    datasource = datasource ?: DatasourceEntity.from(field.dataSource),
                )

                is MobileField -> FieldEntity(
                    name = field.name,
                    fieldType = field.type,
                    required = field.required,
                    datasource = null,
                )

                is NormalField -> FieldEntity(
                    name = field.name,
                    fieldType = field.type,
                    required = field.required,
                    datasource = null,
                )

                is SelectField -> FieldEntity(
                    name = field.name,
                    fieldType = field.type,
                    required = field.required,
                    datasource = null,
                    domain = field.domain,
                )
            }
        }
    }

    fun toDomain(): Field {
        return when (fieldType) {
            FieldType.NORMAL -> NormalField(name, fieldType, required)
            FieldType.MOBILE -> MobileField(name, fieldType, required)
            FieldType.DATA_SOURCE -> DataSourceField(name, datasource!!.toDomain(), fieldType, required)
            FieldType.SINGLE_DOMAIN, FieldType.MULTIPLE_DOMAIN -> SelectField(
                name,
                fieldType,
                domain,
                required,
            )
        }
    }
}
