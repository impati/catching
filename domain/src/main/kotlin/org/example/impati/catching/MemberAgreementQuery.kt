package org.example.impati.catching

import org.example.impati.catching.auth.Member
import org.example.impati.catching.member_agreement.Agreement
import org.example.impati.catching.member_agreement.MemberAgreement
import org.example.impati.catching.member_agreement.MemberAgreementByTerms
import org.example.impati.catching.member_agreement.MemberAgreementRepository
import org.example.impati.catching.terms.TermsGroup
import org.springframework.stereotype.Component

@Component
class MemberAgreementQuery(
    private val memberAgreementRepository: MemberAgreementRepository,
) {

    fun getMemberAgreement(member: Member): MemberAgreement {
        return memberAgreementRepository.findBy(member);
    }

    fun getMemberAgreementBy(member: Member, termsGroup: TermsGroup): MemberAgreementByTerms {
        val memberAgreement = memberAgreementRepository.findBy(member)

        val by = mutableMapOf<String, Boolean>();

        termsGroup.values.forEach {
            val agreement = memberAgreement.get(it.terms.id)
            by[it.terms.id] = agreement?.value ?: false
        }

        return MemberAgreementByTerms(
            member.id,
            termsGroup.type,
            by.entries.map { Agreement(it.key, it.value) }
        )
    }
}
