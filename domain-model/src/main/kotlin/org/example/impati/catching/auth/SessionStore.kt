package org.example.impati.catching.auth

import java.time.Duration

interface SessionStore {

    fun save(sessionId: String, value: String, ttl: Duration)

    fun find(sessionId: String): String?

    fun delete(sessionId: String)
}
