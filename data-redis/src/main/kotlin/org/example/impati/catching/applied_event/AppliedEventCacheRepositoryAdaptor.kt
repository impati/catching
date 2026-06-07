package org.example.impati.catching.applied_event

import org.example.impati.catching.first_come.FirstCome
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class AppliedEventCacheRepositoryAdaptor(
    private val redisTemplate: StringRedisTemplate
) : AppliedEventCacheRepository {

    override fun add(firstCome: FirstCome): Int {
        return redisTemplate.opsForValue()
            .increment(key(firstCome))
            ?.toInt()
            ?: error("Failed to increase applied event count: ${firstCome.id}")
    }

    override fun minus(firstCome: FirstCome): Int {
        return redisTemplate.opsForValue()
            .decrement(key(firstCome))
            ?.toInt()
            ?: error("Failed to decrease applied event count: ${firstCome.id}")
    }

    private fun key(firstCome: FirstCome): String {
        return "first-come:${firstCome.id}:applied-count"
    }
}
