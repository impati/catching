package org.example.impati.catching.api.request

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.example.impati.catching.first_come.*
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CreateFirstComeRequest(

    @field:NotBlank
    val name: String,

    @field:NotNull
    @field:Positive
    val capacity: Int,

    @field:NotNull
    val startAt: LocalDateTime,

    @field:NotNull
    val endAt: LocalDateTime,

    @field:NotNull
    val displayAt: LocalDateTime,

    @field:NotBlank
    val organizer: String,

    val eligibilityValue: String? = null,

    val duplicable: Boolean = true,

    val joinMethod: JoinMethod = JoinMethod.IMMEDIATELY,

    val waitType: WaitType = WaitType.WAITLIST,

    val waitCapacity: Int? = null,
) {

    fun toInputVo(): FirstComeInputVo {
        val waitPolicy = when (waitType) {
            WaitType.WAITLIST ->
                waitCapacity?.let { WaitPolicy.waitlist(it) } ?: WaitPolicy.waitlist()
        }
        return FirstComeInputVo(
            name = FirstComeName(name),
            capacity = FirstComeCapacity(capacity),
            time = FirstComeTime(startAt = startAt, endAt = endAt, displayAt = displayAt),
            eligibility = Eligibility(
                value = eligibilityValue ?: "",
                duplicable = duplicable,
            ),
            join = Join(method = joinMethod),
            waitPolicy = waitPolicy,
            organizer = Organizer(organizer),
        )
    }
}
