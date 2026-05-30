package org.example.impati.catching

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface FirstComeEntityRepository : JpaRepository<FirstComeEntity, String> {

    @Query("SELECT f FROM FirstComeEntity f WHERE f.displayAt <= :now")
    fun findByDisplayable(@Param("now") now: LocalDateTime): List<FirstComeEntity>
}
