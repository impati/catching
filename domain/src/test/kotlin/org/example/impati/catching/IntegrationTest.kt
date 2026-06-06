package org.example.impati.catching

import org.example.impati.catching.applied_event.AlternateEventRepository
import org.example.impati.catching.applied_event.AppliedEventRepository
import org.example.impati.catching.auth.MemberClient
import org.example.impati.catching.first_come.FirstComeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class IntegrationTest {

    @Autowired
    lateinit var firstComeRepository: FirstComeRepository

    @Autowired
    lateinit var appliedEventRepository: AppliedEventRepository

    @Autowired
    lateinit var alternateEventRepository: AlternateEventRepository

    @MockBean
    lateinit var memberClient: MemberClient
}
