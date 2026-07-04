package org.example.impati.catching.member_agreement

import jakarta.persistence.*

@Entity
@Table(name = "member_agreement")
class MemberAgreementEntity(

    @Id
    @Column(name = "member_id", nullable = false, updatable = false)
    var memberId: String,

    @Column(name = "agreements", nullable = false)
    @Convert(converter = AgreementConverter::class)
    var agreements: MutableList<Agreement>
) {

    companion object {
        fun from(memberAgreement: MemberAgreement): MemberAgreementEntity {
            return MemberAgreementEntity(
                memberId = memberAgreement.memberId,
                memberAgreement.agreements
            )
        }
    }

    fun toDomain(): MemberAgreement {
        return MemberAgreement(
            memberId = this.memberId,
            this.agreements
        )
    }
}
