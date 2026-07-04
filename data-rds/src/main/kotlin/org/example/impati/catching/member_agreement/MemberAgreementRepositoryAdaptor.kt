package org.example.impati.catching.member_agreement

import org.example.impati.catching.auth.Member
import org.springframework.stereotype.Repository

@Repository
class MemberAgreementRepositoryAdaptor(
    private val repository: MemberAgreementEntityRepository
) : MemberAgreementRepository {

    override fun save(memberAgreement: MemberAgreement): MemberAgreement {
        return repository.save(MemberAgreementEntity.from(memberAgreement)).toDomain()
    }

    override fun findBy(member: Member): MemberAgreement {
        val entity = repository.findById(member.id)
        if (entity.isPresent) {
            return entity.get().toDomain()
        }

        return MemberAgreement.empty(member.id)
    }
}
