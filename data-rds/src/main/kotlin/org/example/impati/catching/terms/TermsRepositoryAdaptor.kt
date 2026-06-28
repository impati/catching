package org.example.impati.catching.terms

import org.example.impati.catching.terms.exception.NotFoundTermsException
import org.example.impati.catching.terms.exception.NotFoundTermsGroupException
import org.springframework.stereotype.Repository

@Repository
class TermsRepositoryAdaptor(
    private val termsEntityRepository: TermsEntityRepository,
    private val termsGroupEntityRepository: TermsGroupEntityRepository,
) : TermsRepository {

    override fun save(terms: Terms): Terms {
        return termsEntityRepository.save(TermsEntity.from(terms)).toDomain()
    }

    override fun save(termsGroup: TermsGroup): TermsGroup {
        val terms = termsGroup.values.map { findTermsEntity(it.id) }
        val savedTermsGroup = termsGroupEntityRepository.save(TermsGroupEntity.from(termsGroup))

        return savedTermsGroup.toDomain(terms.map { it.toDomain() })
    }

    override fun getTermsGroup(termsGroupType: TermsGroupType): TermsGroup {
        val termsGroup = termsGroupEntityRepository.findById(termsGroupType)
            .orElseThrow { NotFoundTermsGroupException("TermsGroup $termsGroupType not found") }

        return termsGroup.toDomain(findTermsAllBy(termsGroup.termsIds))
    }

    override fun findTermsAll(): List<Terms> {
        return termsEntityRepository.findAll().map { it.toDomain() }
    }

    override fun findBy(termsId: String): Terms {
        return findTermsEntity(termsId).toDomain()
    }

    private fun findTermsEntity(termsId: String): TermsEntity {
        return termsEntityRepository.findById(termsId)
            .orElseThrow { NotFoundTermsException("Terms $termsId not found") }
    }

    private fun findTermsAllBy(termsIds: List<String>): List<Terms> {
        return termsIds.map { findBy(it) }
    }
}
