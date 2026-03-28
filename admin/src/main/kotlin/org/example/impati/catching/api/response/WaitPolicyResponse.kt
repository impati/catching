package org.example.impati.catching.api.response

import org.example.impati.catching.WaitType

data class WaitPolicyResponse(
    val waitType: WaitType,
    val capacity: Int? = 0,
)
