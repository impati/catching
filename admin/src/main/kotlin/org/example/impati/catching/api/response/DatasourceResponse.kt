package org.example.impati.catching.api.response

import org.example.impati.catching.field.Datasource

data class DatasourceResponse(
    val name: String,
    val url: String
) {

    companion object {

        fun from(datasource: Datasource): DatasourceResponse {
            return DatasourceResponse(
                datasource.name,
                datasource.url
            )
        }
    }
}
