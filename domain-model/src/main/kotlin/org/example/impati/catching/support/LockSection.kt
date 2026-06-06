package org.example.impati.catching.support

interface LockSection {

    fun <T> execute(lockName: String, timeoutSeconds: Int = 10, task: () -> T): T
}
