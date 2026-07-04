package org.example.impati.catching.terms

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class TermsInGroupVoConverter : AttributeConverter<List<TermsInGroupVo>, String> {

    private val objectMapper = jacksonObjectMapper()
    private val typeReference = object : TypeReference<List<TermsInGroupVo>>() {
    }

    override fun convertToDatabaseColumn(attribute: List<TermsInGroupVo>?): String {
        return objectMapper.writeValueAsString(attribute ?: emptyList<String>())
    }

    override fun convertToEntityAttribute(dbData: String?): List<TermsInGroupVo> {
        if (dbData.isNullOrBlank()) {
            return emptyList()
        }

        return objectMapper.readValue(dbData, typeReference)
    }
}
