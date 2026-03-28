package org.example.impati.catching

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "first_come")
class FirstComeEntity(

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "capacity", nullable = false)
    var capacity: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: FirstComeStatus,

    @Column(name = "start_at", nullable = false)
    var startAt: LocalDateTime,

    @Column(name = "end_at", nullable = false)
    var endAt: LocalDateTime,

    @Column(name = "display_at", nullable = false)
    var displayAt: LocalDateTime,

    @Column(name = "eligibility", nullable = false)
    var eligibility: String,

    @Column(name = "duplicable", nullable = false)
    var duplicable: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(name = "join_method", nullable = false)
    var joinMethod: JoinMethod,

    @Enumerated(EnumType.STRING)
    @Column(name = "wait_type", nullable = false)
    var waitType: WaitType,

    @Column(name = "wait_capacity")
    var waitCapacity: Int?,

    @Column(name = "organizer", nullable = false)
    var organizer: String

) {
    
    companion object {
        fun from(firstCome: FirstCome): FirstComeEntity {
            return FirstComeEntity(
                id = firstCome.id,
                name = firstCome.name.value,
                capacity = firstCome.capacity.value,
                status = firstCome.status,
                startAt = firstCome.time.startAt,
                endAt = firstCome.time.endAt,
                displayAt = firstCome.time.displayAt,
                eligibility = firstCome.eligibility.value,
                duplicable = firstCome.eligibility.duplicable,
                joinMethod = firstCome.join.method,
                waitType = firstCome.waitPolicy.waitType,
                waitCapacity = firstCome.waitPolicy.capacity,
                organizer = firstCome.organizer.value
            )
        }
    }

    fun toDomain(): FirstCome {
        return FirstCome(
            id = id,
            name = FirstComeName(name),
            capacity = FirstComeCapacity(capacity),
            status = status,
            time = FirstComeTime(
                startAt = startAt,
                endAt = endAt,
                displayAt = displayAt
            ),
            eligibility = Eligibility(
                value = eligibility,
                duplicable = duplicable
            ),
            join = Join(
                method = joinMethod
            ),
            waitPolicy = WaitPolicy(
                waitType = waitType,
                capacity = waitCapacity
            ),
            organizer = Organizer(organizer)
        )
    }
}
