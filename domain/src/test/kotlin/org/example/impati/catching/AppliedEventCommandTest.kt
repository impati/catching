package org.example.impati.catching

import org.assertj.core.api.Assertions
import org.example.impati.catching.applied_event.exception.ApplyFailException
import org.example.impati.catching.auth.Member
import org.example.impati.catching.first_come.FirstComeTime
import org.example.impati.fixture.firstCome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class AppliedEventCommandTest : IntegrationTest() {

    @Autowired
    lateinit var sut: AppliedEventCommand;

    @BeforeEach
    fun setUp() {
        val connection = redisTemplate.connectionFactory!!.connection
        try {
            connection.serverCommands().flushDb()
        } finally {
            connection.close()
        }
    }

    @Test
    fun `선착순 이벤트를 신청한다`() {
        val now = LocalDateTime.now()
        val firstCome = firstCome {
            id = "A"
            time = FirstComeTime(
                now.minusDays(2),
                now.plusDays(3),
                now.minusDays(1),
            )
        }.approve().toActive(now)
        val member = Member("tester", "tester")

        sut.applyFor(firstCome, member)

        Assertions.assertThat(appliedEventRepository.findBy(firstCome, member))
            .extracting { it!!.firstCome.id }
            .isEqualTo(firstCome.id)
    }

    @Test
    fun `선착순 이벤트를 동시에 신청해도 정원만큼만 성공한다`() {
        val now = LocalDateTime.now()
        val capacity = 10
        val applicantCount = 50
        val firstCome = firstCome {
            id = "concurrent-first-come"
            this.capacity = capacity
            time = FirstComeTime(
                now.minusDays(2),
                now.plusDays(3),
                now.minusDays(1),
            )
        }.approve().toActive(now)

        val executorService = Executors.newFixedThreadPool(applicantCount)
        val readyLatch = CountDownLatch(applicantCount)
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(applicantCount)
        val errors = ConcurrentLinkedQueue<Throwable>()

        repeat(applicantCount) { index ->
            executorService.submit {
                try {
                    readyLatch.countDown()
                    startLatch.await()

                    sut.applyFor(firstCome, Member("tester-$index", "tester-$index"))
                } catch (exception: Throwable) {
                    errors.add(exception)
                } finally {
                    doneLatch.countDown()
                }
            }
        }

        readyLatch.await(5, TimeUnit.SECONDS)
        startLatch.countDown()
        doneLatch.await(10, TimeUnit.SECONDS)
        executorService.shutdown()

        Assertions.assertThat(errors)
            .hasSize(applicantCount - capacity)
            .allMatch { it is ApplyFailException }
        Assertions.assertThat(appliedEventRepository.count(firstCome)).isEqualTo(capacity)
    }

    @Test
    fun `Redis 카운터를 사용해 동시에 신청해도 정원만큼만 성공한다`() {
        val now = LocalDateTime.now()
        val capacity = 10
        val applicantCount = 50
        val firstCome = firstCome {
            id = "redis-concurrent-first-come"
            this.capacity = capacity
            time = FirstComeTime(
                now.minusDays(2),
                now.plusDays(3),
                now.minusDays(1),
            )
        }.approve().toActive(now)

        val executorService = Executors.newFixedThreadPool(applicantCount)
        val readyLatch = CountDownLatch(applicantCount)
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(applicantCount)
        val errors = ConcurrentLinkedQueue<Throwable>()

        repeat(applicantCount) { index ->
            executorService.submit {
                try {
                    readyLatch.countDown()
                    startLatch.await()

                    sut.applyForVer2(firstCome, Member("redis-tester-$index", "redis-tester-$index"))
                } catch (exception: Throwable) {
                    errors.add(exception)
                } finally {
                    doneLatch.countDown()
                }
            }
        }

        readyLatch.await(5, TimeUnit.SECONDS)
        startLatch.countDown()
        doneLatch.await(10, TimeUnit.SECONDS)
        executorService.shutdown()

        Assertions.assertThat(errors)
            .hasSize(applicantCount - capacity)
            .allMatch { it is ApplyFailException }
        Assertions.assertThat(appliedEventRepository.count(firstCome)).isEqualTo(capacity)
    }
}
