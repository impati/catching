package org.example.impati.catching.migration

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class MigrationJobExecutor(
    val jobs: List<MigrationJob>
) {

    @EventListener(ApplicationReadyEvent::class)
    fun init() {
        // JVM System Property
        val property = System.getProperty("jobName")
        when {
            property.isNullOrBlank() -> {
                throw IllegalStateException("Job name cannot be null")
            }

            else -> execute(JobName.valueOf(property))
        }
    }

    fun execute(jobName: JobName) {
        for (job in jobs) {
            if (job.name() == jobName) {
                job.run()
            }
        }
    }
}
