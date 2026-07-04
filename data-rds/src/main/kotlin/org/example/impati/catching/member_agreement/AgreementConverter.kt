package org.example.impati.catching.member_agreement

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class AgreementConverter : AttributeConverter<List<Agreement>, String> {

    private val objectMapper = jacksonObjectMapper()
    private val typeReference = object : TypeReference<List<Agreement>>() {
    }

    override fun convertToDatabaseColumn(attribute: List<Agreement>?): String? {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<Agreement>? {
        return objectMapper.readValue(dbData, typeReference)
    }
}
