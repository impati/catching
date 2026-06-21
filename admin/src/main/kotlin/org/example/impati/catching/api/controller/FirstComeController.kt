package org.example.impati.catching.api.controller

import jakarta.validation.Valid
import org.example.impati.catching.FirstComeCommand
import org.example.impati.catching.api.request.CreateFirstComeRequest
import org.example.impati.catching.api.response.FirstComeResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class FirstComeController(
    private val firstComeCommand: FirstComeCommand
) {

    @PostMapping("/v1/comes")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody body: CreateFirstComeRequest): FirstComeResponse {
        val createdFirstCome = firstComeCommand.create(body.toInputVo())
        return FirstComeResponse.from(createdFirstCome)
    }

    @PostMapping("/v1/comes/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    fun approve(@PathVariable id: String): FirstComeResponse {
        val approvedFirstCome = firstComeCommand.approved(id);
        return FirstComeResponse.from(approvedFirstCome)
    }
}
