package org.example.impati.catching

import org.example.impati.catching.first_come.ActiveFirstCome
import org.example.impati.catching.first_come.FirstComeRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class FirstComeQuery(
    private val firstComeRepository: FirstComeRepository
) {

    fun findByActive(now: LocalDateTime): List<ActiveFirstCome> {
        return firstComeRepository.findActiveBy(now);
    }
}
