package org.example.impati.catching.terms

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "terms")
class TermsEntity(

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: String,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    val content: String,
) {

    companion object {

        fun from(terms: Terms): TermsEntity {
            return TermsEntity(
                id = terms.id,
                title = terms.title,
                content = terms.content,
            )
        }
    }

    fun toDomain(): Terms {
        return Terms(
            id = id,
            title = title,
            content = content,
        )
    }
}
