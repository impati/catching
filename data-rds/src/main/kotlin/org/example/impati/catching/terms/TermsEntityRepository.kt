package org.example.impati.catching.terms

import org.springframework.data.jpa.repository.JpaRepository

interface TermsEntityRepository : JpaRepository<TermsEntity, String> {
}
