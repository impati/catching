package org.example.impati.catching

import org.example.impati.catching.first_come.FirstComeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class IntegrationTest {

    @Autowired
    lateinit var firstComeRepository: FirstComeRepository;
}
