package org.example.impati.catching

import org.assertj.core.api.Assertions.assertThat
import org.example.impati.testsupport.fixture.firstComeInput
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreatedFirstComeCommandTest : IntegrationTest() {

    @Autowired
    lateinit var sut: FirstComeCommand;

    @Test
    fun `선착순 이벤트 생성한다`() {
        // given
        val input = firstComeInput()

        // when
        val createdFirstCome = sut.create(input)

        // then
        assertThat(createdFirstCome.id).hasSize(7)
        assertThat(createdFirstCome.name).isEqualTo(input.name)
        assertThat(createdFirstCome.capacity).isEqualTo(input.capacity)
        assertThat(createdFirstCome.time).isEqualTo(input.time)
        assertThat(createdFirstCome.eligibility).isEqualTo(input.eligibility)
        assertThat(createdFirstCome.join).isEqualTo(input.join)
        assertThat(createdFirstCome.waitPolicy).isEqualTo(input.waitPolicy)
        assertThat(createdFirstCome.organizer).isEqualTo(input.organizer)
        val savedFirstCome = firstComeRepository.findById(createdFirstCome.id)
        assertThat(savedFirstCome).isPresent
        assertThat(savedFirstCome.get().name).isEqualTo(input.name)
        assertThat(savedFirstCome.get().capacity).isEqualTo(input.capacity)
    }
}
