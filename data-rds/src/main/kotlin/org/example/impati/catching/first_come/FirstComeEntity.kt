package org.example.impati.catching.first_come

import jakarta.persistence.*
import org.example.impati.catching.field.Field
import org.example.impati.catching.field.StringListConverter
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

    @Column(name = "approved", nullable = false)
    var approved: Boolean,

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

    @Convert(converter = StringListConverter::class)
    @Column(name = "fields", nullable = false, columnDefinition = "TEXT")
    var fields: List<String> = emptyList(),

    @Column(name = "organizer", nullable = false)
    var organizer: String
) {

    companion object {
        fun from(createdFirstCome: CreatedFirstCome): FirstComeEntity {
            return FirstComeEntity(
                id = createdFirstCome.id,
                name = createdFirstCome.name.value,
                capacity = createdFirstCome.capacity.value,
                approved = false,
                startAt = createdFirstCome.time.startAt,
                endAt = createdFirstCome.time.endAt,
                displayAt = createdFirstCome.time.displayAt,
                eligibility = createdFirstCome.eligibility.value,
                duplicable = createdFirstCome.eligibility.duplicable,
                joinMethod = createdFirstCome.join.method,
                waitType = createdFirstCome.waitPolicy.waitType,
                waitCapacity = createdFirstCome.waitPolicy.capacity,
                fields = createdFirstCome.fields.map { it.name },
                organizer = createdFirstCome.organizer.value
            )
        }

        fun from(createdFirstCome: ApprovedFirstCome): FirstComeEntity {
            return FirstComeEntity(
                id = createdFirstCome.id,
                name = createdFirstCome.name.value,
                capacity = createdFirstCome.capacity.value,
                approved = true,
                startAt = createdFirstCome.time.startAt,
                endAt = createdFirstCome.time.endAt,
                displayAt = createdFirstCome.time.displayAt,
                eligibility = createdFirstCome.eligibility.value,
                duplicable = createdFirstCome.eligibility.duplicable,
                joinMethod = createdFirstCome.join.method,
                waitType = createdFirstCome.waitPolicy.waitType,
                waitCapacity = createdFirstCome.waitPolicy.capacity,
                fields = createdFirstCome.fields.map { it.name },
                organizer = createdFirstCome.organizer.value
            )
        }
    }

    fun toCreated(fields: List<Field>): CreatedFirstCome {
        check(!approved) { "already approved" }

        return CreatedFirstCome(
            id = id,
            name = FirstComeName(name),
            capacity = FirstComeCapacity(capacity),
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
            fields = fields,
            organizer = Organizer(organizer)
        )
    }

    fun toApproved(fields: List<Field>): ApprovedFirstCome {
        check(approved) { "must be approved" }

        return ApprovedFirstCome(
            id = id,
            name = FirstComeName(name),
            capacity = FirstComeCapacity(capacity),
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
            fields = fields,
            organizer = Organizer(organizer)
        )
    }

    fun toActive(
        now: LocalDateTime,
        fields: List<Field>
    ): ActiveFirstCome {
        return toApproved(fields).toActive(now)
    }
}
