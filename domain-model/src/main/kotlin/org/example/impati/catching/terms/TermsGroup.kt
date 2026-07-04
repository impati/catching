package org.example.impati.catching.terms

class TermsGroup(
    val type: TermsGroupType,
    val values: List<TermsInGroup>
) {

    companion object {

        fun create(type: TermsGroupType, termsList: List<TermsInGroup>): TermsGroup {
            return TermsGroup(
                type,
                termsList
            )
        }
    }

    fun contain(termsId: String): Boolean {
        return values.map { it.terms.id }.contains(termsId)
    }
}
