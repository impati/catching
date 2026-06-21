package org.example.impati.catching.field

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FieldTest {


    @Test
    fun `노말 필드`() {
        val field = NormalField("이름", FieldType.NORMAL)

        assertThat(field.name).isEqualTo("이름")
        assertThat(field.type).isEqualTo(FieldType.NORMAL)
    }

    @Test
    fun `휴대폰 필드`() {
        val field = MobileField("휴대전화")

        assertThat(field.name).isEqualTo("휴대전화")
        assertThat(field.type).isEqualTo(FieldType.MOBILE)
    }

    @Test
    fun `주소 필드`() {
        val dataSource = DataSource("주소 데이터소스", "https://test.com")
        val field = DataSourceField(
            "주소",
            dataSource
        )

        assertThat(field.name).isEqualTo("주소")
        assertThat(field.type).isEqualTo(FieldType.DATA_SOURCE)
        assertThat(field.dataSource.url).isEqualTo("https://test.com")
    }

    @Test
    fun `단일 선택 필드`() {
        val field = SelectField(
            "신청구분",
            FieldType.SINGLE_DOMAIN,
            listOf("개인", "단체")
        )

        assertThat(field.name).isEqualTo("신청구분")
        assertThat(field.type).isEqualTo(FieldType.SINGLE_DOMAIN)
        assertThat(field.domain).contains("개인", "단체")
    }

    @Test
    fun `멀티 선택 필드`() {
        val field = SelectField(
            "신청구분",
            FieldType.MULTIPLE_DOMAIN,
            listOf("개인", "단체")
        )

        assertThat(field.name).isEqualTo("신청구분")
        assertThat(field.type).isEqualTo(FieldType.MULTIPLE_DOMAIN)
        assertThat(field.domain).contains("개인", "단체")
    }
}
