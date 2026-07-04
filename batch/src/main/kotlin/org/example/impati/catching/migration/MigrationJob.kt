package org.example.impati.catching.migration

interface MigrationJob {

    fun run()

    fun name(): JobName
}
