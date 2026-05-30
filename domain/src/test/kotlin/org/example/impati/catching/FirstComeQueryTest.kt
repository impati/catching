package org.example.impati.catching

import org.assertj.core.api.Assertions.assertThat
import org.example.impati.catching.first_come.FirstComeTime
import org.example.impati.testsupport.fixture.firstCome
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class FirstComeQueryTest : IntegrationTest() {

    @Autowired
    lateinit var sut: FirstComeQuery;

    @Test
    fun `노출일시보다 같거나 큰 선착순 이벤트를 가져온다`() {
        val now = LocalDateTime.now()
        val firstComeA = firstCome {
            id = "A"
            time = FirstComeTime(
                now.minusDays(2),
                now.plusDays(3),
                now.minusDays(1),
            )
        }
        val firstComeB = firstCome {
            id = "B"
            time = FirstComeTime(
                now.minusDays(1),
                now.plusDays(3),
                now
            )
        }
        val firstComeC = firstCome {
            id = "C"
            time = FirstComeTime(
                now.minusDays(2),
                now.plusDays(3),
                now.plusDays(1)
            )
        }
        firstComeRepository.save(firstComeA)
        firstComeRepository.save(firstComeB)
        firstComeRepository.save(firstComeC)

        val result = sut.findByDisplayable(now);

        assertThat(result).extracting("id")
            .containsExactlyInAnyOrder(firstComeA.id, firstComeB.id)
    }
}
