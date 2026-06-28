package org.example.impati.catching

import org.example.impati.catching.terms.Terms
import org.example.impati.catching.terms.TermsGroup
import org.example.impati.catching.terms.TermsGroupType
import org.example.impati.catching.terms.TermsRepository
import org.springframework.stereotype.Component

@Component
class TermsCommand(
    private val termsRepository: TermsRepository
) {

    fun create(title: String, content: String): Terms {
        return termsRepository.save(Terms.create(title, content))
    }

    fun updateTermsGroup(type: TermsGroupType, termsIds: List<String>): TermsGroup {
        val terms = termsIds.map { termsRepository.findBy(it) }
        return termsRepository.save(TermsGroup.create(type, terms))
    }
}
