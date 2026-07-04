package org.example.impati.catching.migration.job

import org.example.impati.catching.migration.JobName
import org.example.impati.catching.migration.MigrationJob
import org.example.impati.catching.terms.TermsGroupEntity
import org.example.impati.catching.terms.TermsGroupEntityRepository
import org.springframework.stereotype.Component


@Component
class TermsGroupEntityMigrationJob(
    private val termsGroupEntityRepository: TermsGroupEntityRepository
) : MigrationJob {

    override fun run() {
        val entities = read()

        val target = mutableListOf<TermsGroupEntity>()
        for (entity in entities) {
            val element = processor(entity) ?: continue

            target.add(element)
        }

        write(target)
    }

    override fun name(): JobName {
        return JobName.TERMS_GROUP_MIGRATION_V1
    }

    fun read(): List<TermsGroupEntity> {
        return termsGroupEntityRepository.findAll()
    }

    fun processor(entity: TermsGroupEntity): TermsGroupEntity? {
//        val vo = entity.termsIds.map { TermsInGroupVo(it, true) }
//        return TermsGroupEntity(
//            entity.type,
//            entity.termsIds,
//            vo
//        )
        return null;
    }

    fun write(entities: List<TermsGroupEntity>) {
        termsGroupEntityRepository.saveAll(entities)
    }
}
