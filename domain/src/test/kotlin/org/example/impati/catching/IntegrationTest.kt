package org.example.impati.catching

import org.example.impati.catching.applied_event.AlternateEventRepository
import org.example.impati.catching.applied_event.AppliedEventCacheRepository
import org.example.impati.catching.applied_event.AppliedEventRepository
import org.example.impati.catching.auth.MemberClient
import org.example.impati.catching.first_come.FirstComeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest
abstract class IntegrationTest {

    companion object {

        val mysql: MySQLContainer<*> = MySQLContainer(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("catching")
            .withUsername("test")
            .withPassword("test")

        val redis: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379)

        init {
            mysql.start()
            redis.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { mysql.jdbcUrl }
            registry.add("spring.datasource.username") { mysql.username }
            registry.add("spring.datasource.password") { mysql.password }
            registry.add("spring.datasource.driver-class-name") { mysql.driverClassName }
            registry.add("spring.jpa.database-platform") { "org.hibernate.dialect.MySQLDialect" }
            registry.add("spring.datasource.hikari.maximum-pool-size") { 80 }
            registry.add("spring.datasource.hikari.connection-timeout") { 60_000 }

            registry.add("spring.data.redis.host") { redis.host }
            registry.add("spring.data.redis.port") { redis.getMappedPort(6379) }
        }
    }

    @Autowired
    lateinit var firstComeRepository: FirstComeRepository

    @Autowired
    lateinit var appliedEventRepository: AppliedEventRepository

    @Autowired
    lateinit var alternateEventRepository: AlternateEventRepository

    @MockBean
    lateinit var memberClient: MemberClient

    @Autowired
    lateinit var appliedEventCacheRepository: AppliedEventCacheRepository

    @Autowired
    lateinit var redisTemplate: StringRedisTemplate
}
