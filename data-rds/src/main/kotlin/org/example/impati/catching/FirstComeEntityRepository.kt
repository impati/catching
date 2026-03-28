package org.example.impati.catching

import org.springframework.data.jpa.repository.JpaRepository

interface FirstComeEntityRepository : JpaRepository<FirstComeEntity, String> {
}
