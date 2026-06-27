package org.example.impati.catching.field

import org.springframework.data.jpa.repository.JpaRepository

interface FieldEntityRepository : JpaRepository<FieldEntity, String> {
}
