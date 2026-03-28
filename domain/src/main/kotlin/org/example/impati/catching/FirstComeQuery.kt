package org.example.impati.catching

import org.springframework.stereotype.Component

@Component
class FirstComeQuery(
    private val firstComeRepository: FirstComeRepository
) {

    fun find(id: String): FirstCome {
        return firstComeRepository.findById(id).orElseThrow { throw IllegalArgumentException("존재하지 않는 정보입니다.") }
    }

    fun findAll(): List<FirstCome> {
        return firstComeRepository.findAll();
    }
}
