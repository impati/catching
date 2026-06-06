package org.example.impati.catching.applied_event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "alternate_event")
class AlternateEventEntity(

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
        fun from(alternateEvent: AlternateEvent): AlternateEventEntity {
            return AlternateEventEntity(
                id = UUID.randomUUID().toString(),
                firstComeId = alternateEvent.firstCome.id,
                memberId = alternateEvent.member.id,
                createdAt = LocalDateTime.now()
            )
        }
    }
}
