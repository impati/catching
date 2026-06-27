package org.example.impati.catching.field

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(
    name = "datasource",
)
class DatasourceEntity(

    @Id
    @Column(name = "name", nullable = false, unique = false)
    val name: String,

    @Column(name = "url", nullable = false)
    val url: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "created_by", nullable = false)
    val createdBy: String,
) {

    companion object {

        fun from(domain: Datasource): DatasourceEntity {
            return DatasourceEntity(
                domain.name,
                domain.url,
                domain.createdAt,
                domain.createdBy,
            )
        }
    }

    fun toDomain(): Datasource {
        return Datasource(
            name,
            url,
            createdBy,
            createdAt
        )
    }
}
