package org.example.impati.catching

import org.example.impati.catching.auth.Member
import org.example.impati.catching.member_agreement.Agreement
import org.example.impati.catching.member_agreement.MemberAgreement
import org.example.impati.catching.member_agreement.MemberAgreementRepository
import org.example.impati.catching.terms.Terms
import org.springframework.stereotype.Component

@Component
class MemberAgreementCommand(
    private val memberAgreementRepository: MemberAgreementRepository
) {

    fun agree(member: Member, terms: List<Terms>): MemberAgreement {
        val memberAgreement = memberAgreementRepository.findBy(member)

        terms.map { Agreement(it.id, true) }.forEach { memberAgreement.addAgreement(it) }

        return memberAgreementRepository.save(memberAgreement)
    }

    fun disagree(member: Member, terms: List<Terms>): MemberAgreement {
        val memberAgreement = memberAgreementRepository.findBy(member)

        terms.map { Agreement(it.id, false) }.forEach { memberAgreement.addAgreement(it) }

        return memberAgreementRepository.save(memberAgreement)
    }
}
