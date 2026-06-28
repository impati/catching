package org.example.impati.catching.terms

import java.util.*

class Terms(
    val id: String,
    val title: String,
    val content: String,
) {
    companion object {

        fun create(title: String, content: String): Terms {
            val id = UUID.randomUUID().toString()
            return Terms(id, title, content);
        }
    }
}
