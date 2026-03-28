package org.example.impati.catching.api.controller

import jakarta.validation.Valid
import org.example.impati.catching.FirstComeCommand
import org.example.impati.catching.api.request.CreateFirstComeRequest
import org.example.impati.catching.api.response.FirstComeResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1")
class FirstComeWriteController(
    private val firstComeCommand: FirstComeCommand,
) {

    @PostMapping("/comes")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody body: CreateFirstComeRequest): FirstComeResponse {
        val created = firstComeCommand.create(body.toInputVo())
        return FirstComeResponse.from(created)
    }
}
