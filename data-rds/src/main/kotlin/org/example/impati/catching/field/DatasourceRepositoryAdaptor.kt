package org.example.impati.catching.field

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DatasourceRepositoryAdaptor(
    private val repository: DatasourceEntityRepository
) : DatasourceRepository {

    override fun save(dataSource: Datasource): Datasource {
        return repository.save(DatasourceEntity.from(dataSource)).toDomain()
    }

    override fun findById(name: String): Optional<Datasource> {
        return repository.findById(name).map { it.toDomain() };
    }

    override fun findAll(): List<Datasource> {
        return repository.findAll().map { it.toDomain() }
    }
}
