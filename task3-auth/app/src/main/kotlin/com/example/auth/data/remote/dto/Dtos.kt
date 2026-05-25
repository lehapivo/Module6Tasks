package com.example.auth.data.remote.dto

import com.example.auth.domain.model.LoginResult
import com.example.auth.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val username: String,
    val password: String,
    val expiresInMins: Int = 30
)

@Serializable
data class LoginResponseDto(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val image: String = "",
    val token: String = "",
    val refreshToken: String = ""
)

@Serializable
data class UsersResponseDto(
    val users: List<UserDto> = emptyList()
)

@Serializable
data class UserDto(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val image: String = ""
)

fun LoginResponseDto.toDomain() = LoginResult(
    token = token,
    user = User(id, firstName, lastName, username, email, image)
)

fun UserDto.toDomain() = User(id, firstName, lastName, username, email, image)
