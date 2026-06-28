package org.example.impati.catching.terms

interface TermsRepository {

    fun save(terms: Terms): Terms

    fun save(termsGroup: TermsGroup): TermsGroup

    fun getTermsGroup(termsGroupType: TermsGroupType): TermsGroup

    fun findTermsAll(): List<Terms>

    fun findBy(termsId: String): Terms
}
