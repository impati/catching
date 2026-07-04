package org.example.impati.catching.terms

import jakarta.persistence.*
import org.example.impati.catching.field.StringListConverter

@Entity
@Table(name = "terms_group")
class TermsGroupEntity(

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    val type: TermsGroupType,

    @Convert(converter = TermsInGroupVoConverter::class)
    @Column(name = "terms", nullable = false, columnDefinition = "TEXT")
    val terms: List<TermsInGroupVo> = emptyList(),
) {

    companion object {

        fun from(termsGroup: TermsGroup): TermsGroupEntity {
            return TermsGroupEntity(
                type = termsGroup.type,
                terms = termsGroup.values.map { TermsInGroupVo(it.terms.id, it.required) },
            )
        }
    }

    fun toDomain(terms: List<Terms>): TermsGroup {
        return TermsGroup(
            type = type,
            values = this.terms.map {
                TermsInGroup(
                    terms.find { th -> th.id == it.termsId }!!,
                    it.required
                )
            }
        )
    }
}
