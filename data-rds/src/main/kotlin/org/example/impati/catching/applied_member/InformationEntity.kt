package org.example.impati.catching.applied_member

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.impati.catching.field.Information
import org.example.impati.catching.field.StringListConverter
import java.util.UUID

@Entity
@Table(name = "applied_member_information")
class InformationEntity(

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Convert(converter = StringListConverter::class)
    @Column(name = "information_values", nullable = false, columnDefinition = "TEXT")
    val values: List<String> = emptyList(),
) {

    companion object {

        fun from(information: Information): InformationEntity {
            return InformationEntity(
                id = UUID.randomUUID().toString(),
                name = information.name,
                values = information.values,
            )
        }
    }

    fun toDomain(): Information {
        return Information(
            name = name,
            values = values,
        )
    }
}
