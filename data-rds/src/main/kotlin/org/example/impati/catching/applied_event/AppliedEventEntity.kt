package org.example.impati.catching.applied_event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "applied_event",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_applied_event_first_come_member",
            columnNames = ["first_come_id", "member_id"]
        )
    ]
)
class AppliedEventEntity(

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: String,

    @Column(name = "first_come_id", nullable = false)
    val firstComeId: String,

    @Column(name = "member_id", nullable = false)
    val memberId: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime
) {
    companion object {

        fun from(appliedEvent: AppliedEvent): AppliedEventEntity {
            return AppliedEventEntity(
                id = UUID.randomUUID().toString(),
                firstComeId = appliedEvent.firstCome.id,
                memberId = appliedEvent.member.id,
                createdAt = LocalDateTime.now()
            )
        }
    }
}
