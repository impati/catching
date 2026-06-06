package org.example.impati.catching

import org.assertj.core.api.Assertions.assertThat
import org.example.impati.catching.first_come.FirstComeTime
import org.example.impati.fixture.firstCome
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class CreatedFirstComeQueryTest : IntegrationTest() {

    @Autowired
    lateinit var sut: FirstComeQuery;

    @Autowired
    lateinit var command: FirstComeCommand;

    @Test
    fun `승인되었고 활성 시간에 포함된 선착순 이벤트를 가져온다`() {
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
        command.approved(firstComeA.id)
        command.approved(firstComeB.id)
        command.approved(firstComeC.id)

        val result = sut.findByActive(now);

        assertThat(result).extracting("id")
            .containsExactlyInAnyOrder(firstComeA.id, firstComeB.id)
    }
}
