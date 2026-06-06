package org.example.impati.catching.applied_event

import org.example.impati.catching.first_come.FirstCome
import org.springframework.stereotype.Repository

@Repository
class AlternateEventRepositoryAdaptor(
    private val alternateEventEntityRepository: AlternateEventEntityRepository
) : AlternateEventRepository {

    override fun save(alternateEvent: AlternateEvent) {
        alternateEventEntityRepository.save(AlternateEventEntity.from(alternateEvent))
    }

    override fun count(firstCome: FirstCome): Int {
        return alternateEventEntityRepository.countByFirstComeId(firstCome.id).toInt()
    }
}
