package org.example.impati.catching.field

import org.example.impati.catching.field.exception.NotFoundFieldException
import org.example.impati.catching.field.exception.NotFoundDataSourceException
import org.springframework.stereotype.Repository

@Repository
class FieldRepositoryAdaptor(
    private val fieldEntityRepository: FieldEntityRepository,
    private val datasourceEntityRepository: DatasourceEntityRepository,
) : FieldRepository {

    override fun save(field: Field): Field {
        val datasource = if (field is DataSourceField) {
            datasourceEntityRepository.findById(field.dataSource.name)
                .orElseThrow { NotFoundDataSourceException("Datasource ${field.dataSource.name} not found") }
        } else {
            null
        }

        return fieldEntityRepository.save(FieldEntity.from(field, datasource)).toDomain()
    }

    override fun findAll(): List<Field> {
        return fieldEntityRepository.findAll().map { it.toDomain() }
    }

    override fun findByField(fieldName: String): Field {
        return fieldEntityRepository.findById(fieldName)
            .orElseThrow { NotFoundFieldException("Field $fieldName not found") }
            .toDomain()
    }
}
