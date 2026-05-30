package org.example.impati.catching.api.controller

import jakarta.validation.Valid
import org.example.impati.catching.FirstComeCommand
import org.example.impati.catching.FirstComeQuery
import org.example.impati.catching.api.request.CreateFirstComeRequest
import org.example.impati.catching.api.response.FirstComeResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1")
class FirstComeController(
    private val firstComeCommand: FirstComeCommand,
    private val firstComeQuery: FirstComeQuery
) {

    @PostMapping("/comes")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody body: CreateFirstComeRequest): FirstComeResponse {
        val createdFirstCome = firstComeCommand.create(body.toInputVo())
        return FirstComeResponse.from(createdFirstCome)
    }

    @GetMapping("/comes")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<FirstComeResponse> {
        return firstComeQuery.findAll().map { FirstComeResponse.from(it) }
    }
}
