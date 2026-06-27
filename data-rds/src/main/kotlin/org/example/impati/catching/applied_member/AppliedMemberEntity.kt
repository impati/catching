package org.example.impati.catching.applied_member

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID

@Entity
@Table(
    name = "applied_member",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_applied_member_first_come_member",
            columnNames = ["first_come_id", "member_id"]
        )
    ]
)
class AppliedMemberEntity(

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: String,

    @Column(name = "first_come_id", nullable = false)
    val firstComeId: String,

    @Column(name = "member_id", nullable = false)
    val memberId: String,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "applied_member_id")
    val informations: MutableList<InformationEntity> = mutableListOf(),
) {

    companion object {

        fun from(appliedMember: AppliedMember): AppliedMemberEntity {
            return AppliedMemberEntity(
                id = UUID.randomUUID().toString(),
                firstComeId = appliedMember.comeId,
                memberId = appliedMember.memberId,
                informations = appliedMember.informations.map { InformationEntity.from(it) }.toMutableList(),
            )
        }
    }

    fun toDomain(): AppliedMember {
        return AppliedMember(
            comeId = firstComeId,
            memberId = memberId,
            informations = informations.map { it.toDomain() },
        )
    }

    fun update(appliedMember: AppliedMember): AppliedMemberEntity {
        informations.clear()
        informations.addAll(appliedMember.informations.map { InformationEntity.from(it) })
        return this
    }
}
