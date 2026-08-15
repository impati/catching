package org.example.impati.catching.auth

interface MemberClient {

    fun findMember(accessToken: String): Member
}
