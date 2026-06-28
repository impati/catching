package org.example.impati.catching.terms

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.impati.catching.field.StringListConverter

@Entity
@Table(name = "terms_group")
class TermsGroupEntity(

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    val type: TermsGroupType,

    @Convert(converter = StringListConverter::class)
    @Column(name = "terms_ids", nullable = false, columnDefinition = "TEXT")
    val termsIds: List<String> = emptyList(),
) {

    companion object {

        fun from(termsGroup: TermsGroup): TermsGroupEntity {
            return TermsGroupEntity(
                type = termsGroup.type,
                termsIds = termsGroup.values.map { it.id },
            )
        }
    }

    fun toDomain(terms: List<Terms>): TermsGroup {
        return TermsGroup(
            type = type,
            values = terms,
        )
    }
}
