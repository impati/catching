package org.example.impati.catching.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class DatasourceController {

    @GetMapping("/v1/datasource/address")
    fun address(): String {
        return "경기도 안양시 동안구 학의로 120"
    }
}
