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
}
