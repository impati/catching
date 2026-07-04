package org.example.impati.catching.member_agreement

import org.example.impati.catching.auth.Member

interface MemberAgreementRepository {

    fun save(memberAgreement: MemberAgreement): MemberAgreement

    fun findBy(member: Member): MemberAgreement
}
