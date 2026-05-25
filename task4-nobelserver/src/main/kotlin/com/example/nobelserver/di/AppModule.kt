package com.example.nobelserver.di

import com.example.nobelserver.data.repository.InMemoryNobelRepository
import com.example.nobelserver.data.repository.InMemoryUserRepository
import com.example.nobelserver.domain.repository.NobelRepository
import com.example.nobelserver.domain.repository.UserRepository

object AppModule {
    val nobelRepository: NobelRepository = InMemoryNobelRepository()
    val userRepository: UserRepository = InMemoryUserRepository()
}
