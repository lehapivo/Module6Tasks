package com.example.auth.data.repository

import com.example.auth.data.remote.KtorClient
import com.example.auth.data.remote.dto.*
import com.example.auth.domain.model.LoginResult
import com.example.auth.domain.model.User
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

private const val BASE = "https://dummyjson.com"

class AuthRepositoryImpl {
    suspend fun login(username: String, password: String): LoginResult {
        val resp: LoginResponseDto = KtorClient.client.post("$BASE/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(username, password))
        }.body()
        return resp.toDomain()
    }
}

class UserRepositoryImpl {
    suspend fun getUsers(token: String): List<User> {
        val resp: UsersResponseDto = KtorClient.client.get("$BASE/users") {
            header("Authorization", "Bearer $token")
        }.body()
        return resp.users.map { it.toDomain() }
    }

    suspend fun getUserById(id: Int, token: String): User {
        val dto: UserDto = KtorClient.client.get("$BASE/users/$id") {
            header("Authorization", "Bearer $token")
        }.body()
        return dto.toDomain()
    }
}
