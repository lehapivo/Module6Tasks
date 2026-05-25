package com.example.nobelserver.domain.repository

import com.example.nobelserver.domain.model.NobelPrize
import com.example.nobelserver.domain.model.User

interface NobelRepository {
    fun getAllPrizes(): List<NobelPrize>
    fun getPrize(year: String, category: String): NobelPrize?
    fun getLaureates(year: String, category: String) = getPrize(year, category)?.laureates
}

interface UserRepository {
    fun findByUsername(username: String): User?
}
