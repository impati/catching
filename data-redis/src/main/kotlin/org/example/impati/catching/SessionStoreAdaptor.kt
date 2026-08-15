package org.example.impati.catching

import org.example.impati.catching.auth.SessionStore
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class SessionStoreAdaptor(
    private val redisTemplate: StringRedisTemplate
) : SessionStore {

    override fun save(sessionId: String, value: String, ttl: Duration) {
        redisTemplate.opsForValue().set(sessionId, value, ttl)
    }

    override fun find(sessionId: String): String? {
        return redisTemplate.opsForValue().get(sessionId)
    }

    override fun delete(sessionId: String) {
        redisTemplate.delete(sessionId)
    }
}
