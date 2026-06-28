package org.example.impati.catching

import org.example.impati.catching.terms.Terms
import org.example.impati.catching.terms.TermsGroup
import org.example.impati.catching.terms.TermsGroupType
import org.example.impati.catching.terms.TermsRepository
import org.springframework.stereotype.Component

@Component
class TermsQuery(
    private val termsRepository: TermsRepository
) {

    fun allTerms(): List<Terms> {
        return termsRepository.findTermsAll()
    }

    fun getTermsGroup(type: TermsGroupType): TermsGroup {
        return termsRepository.getTermsGroup(type)
    }
}
