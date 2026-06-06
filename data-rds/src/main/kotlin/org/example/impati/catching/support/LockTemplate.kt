package org.example.impati.catching.support

import org.springframework.jdbc.core.ConnectionCallback
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class LockTemplate(
    private val jdbcTemplate: JdbcTemplate
) : LockSection {

    override fun <T> execute(lockName: String, timeoutSeconds: Int, task: () -> T): T {
        val result = jdbcTemplate.execute(ConnectionCallback<T> { connection ->
            val acquired = connection.prepareStatement("SELECT GET_LOCK(?, ?)").use { ps ->
                ps.setString(1, lockName)
                ps.setInt(2, timeoutSeconds)

                ps.executeQuery().use { rs ->
                    rs.next()
                    rs.getInt(1) == 1
                }
            }

            check(acquired) { "Failed to acquire lock: $lockName" }

            try {
                task()
            } finally {
                connection.prepareStatement("SELECT RELEASE_LOCK(?)").use { ps ->
                    ps.setString(1, lockName)
                    ps.executeQuery().use { }
                }
            }
        })

        @Suppress("UNCHECKED_CAST")
        return result as T
    }
}
