package org.example.impati.catching

import org.example.impati.catching.first_come.FirstCome
import org.example.impati.catching.first_come.FirstComeRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

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

    fun findByDisplayable(now: LocalDateTime): List<FirstCome> {
        return firstComeRepository.findByDisplayable(now);
    }
}
