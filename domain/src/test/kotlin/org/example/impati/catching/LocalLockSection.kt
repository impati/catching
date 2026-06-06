package org.example.impati.catching

import org.example.impati.catching.support.LockSection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.locks.ReentrantLock

@Primary
@Component
class LocalLockSection(
    private val locks: ConcurrentMap<String, ReentrantLock> = ConcurrentHashMap()
) : LockSection {

    override fun <T> execute(lockName: String, timeoutSeconds: Int, task: () -> T): T {
        val lock = locks.computeIfAbsent(lockName) { ReentrantLock() }
        val acquired = lock.tryLock(timeoutSeconds.toLong(), TimeUnit.SECONDS)
        if (!acquired) {
            throw TimeoutException("Lock acquisition timed out after $timeoutSeconds seconds")
        }

        try {
            return task()
        } finally {
            lock.unlock()
        }
    }
}
