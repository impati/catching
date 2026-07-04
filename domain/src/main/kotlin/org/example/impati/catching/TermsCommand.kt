package org.example.impati.catching

import org.example.impati.catching.terms.*
import org.springframework.stereotype.Component

@Component
class TermsCommand(
    private val termsRepository: TermsRepository
) {

    fun create(title: String, content: String): Terms {
        return termsRepository.save(Terms.create(title, content))
    }

    fun createTermsGroup(type: TermsGroupType, termsWithRequired: Map<String, Boolean>): TermsGroup {
        val termsInGroup = termsWithRequired.entries.map {
            TermsInGroup(termsRepository.findBy(it.key), it.value)
        }

        return termsRepository.save(TermsGroup.create(type, termsInGroup))
    }
}
