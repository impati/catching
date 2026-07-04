package org.example.impati.catching.terms

class TermsGroup(
    val type: TermsGroupType,
    val values: List<Terms>
) {

    companion object {

        fun create(type: TermsGroupType, termsList: List<Terms>): TermsGroup {
            return TermsGroup(
                type,
                termsList
            )
        }
    }

    fun contain(termsId: String): Boolean {
        return values.map { it.id }.contains(termsId)
    }
}
