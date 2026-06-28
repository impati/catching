package org.example.impati.catching.applied_member

import org.springframework.data.jpa.repository.JpaRepository

interface AppliedMemberEntityRepository : JpaRepository<AppliedMemberEntity, String> {

    fun findByFirstComeIdAndMemberId(
        firstComeId: String,
        memberId: String,
    ): AppliedMemberEntity?

    fun existsByFirstComeIdAndMemberId(
        firstComeId: String,
        memberId: String,
    ): Boolean
}
