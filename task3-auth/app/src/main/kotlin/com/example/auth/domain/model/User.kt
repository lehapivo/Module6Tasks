package com.example.auth.domain.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val image: String
)

data class LoginResult(
    val token: String,
    val user: User
)
