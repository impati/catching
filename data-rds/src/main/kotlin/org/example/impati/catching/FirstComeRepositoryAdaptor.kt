package org.example.impati.catching

import org.example.impati.catching.first_come.FirstCome
import org.example.impati.catching.first_come.FirstComeRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class FirstComeRepositoryAdaptor(
    private val firstComeEntityRepository: FirstComeEntityRepository
) : FirstComeRepository {

    override fun save(firstCome: FirstCome): FirstCome {
        return firstComeEntityRepository.save(FirstComeEntity.from(firstCome)).toDomain();
    }

    override fun findById(id: String): Optional<FirstCome> {
        return firstComeEntityRepository.findById(id).map { it.toDomain() };
    }

    override fun findAll(): List<FirstCome> {
        return firstComeEntityRepository.findAll().map { it.toDomain() }
    }
}
