package org.example.impati.catching.field

import org.springframework.data.jpa.repository.JpaRepository

interface DatasourceEntityRepository : JpaRepository<DatasourceEntity, String> {
}
