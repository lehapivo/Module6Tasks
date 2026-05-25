package com.example.nobelserver.data.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.nobelserver.data.database.*
import com.example.nobelserver.domain.model.Laureate
import com.example.nobelserver.domain.model.NobelPrize
import com.example.nobelserver.domain.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

class NobelRepositoryImpl {
    suspend fun getAllPrizes(): List<NobelPrize> = newSuspendedTransaction {
        PrizeTable.selectAll().map { it.toNobelPrize() }
    }

    suspend fun getPrizeById(id: Int): NobelPrize? = newSuspendedTransaction {
        PrizeTable.selectAll()
            .where { PrizeTable.id eq id }
            .map { it.toNobelPrize() }
            .firstOrNull()
    }

    suspend fun getLaureatesByPrize(prizeId: Int): List<Laureate> = newSuspendedTransaction {
        LaureateTable.selectAll()
            .where { LaureateTable.prizeId eq prizeId }
            .map { it.toLaureate() }
    }

    private fun ResultRow.toNobelPrize() = NobelPrize(
        id = this[PrizeTable.id],
        awardYear = this[PrizeTable.awardYear],
        category = this[PrizeTable.category],
        fullName = this[PrizeTable.fullName],
        motivation = this[PrizeTable.motivation],
        detailLink = this[PrizeTable.detailLink]
    )

    private fun ResultRow.toLaureate() = Laureate(
        id = this[LaureateTable.id],
        prizeId = this[LaureateTable.prizeId],
        fullName = this[LaureateTable.fullName],
        portion = this[LaureateTable.portion],
        motivation = this[LaureateTable.motivation],
        portraitUrl = this[LaureateTable.portraitUrl]
    )
}

class UserRepositoryImpl {
    suspend fun findByUsername(username: String): User? = newSuspendedTransaction {
        UserTable.selectAll()
            .where { UserTable.username eq username }
            .map {
                User(
                    id = it[UserTable.id],
                    username = it[UserTable.username],
                    passwordHash = it[UserTable.passwordHash],
                    role = it[UserTable.role]
                )
            }
            .firstOrNull()
    }

    fun verifyPassword(plain: String, hash: String): Boolean =
        BCrypt.verifyer().verify(plain.toCharArray(), hash).verified
}

class UserPrizeRepositoryImpl {
    suspend fun getFavorites(userId: Int): List<NobelPrize> = newSuspendedTransaction {
        (UserPrizeTable innerJoin PrizeTable)
            .selectAll()
            .where { UserPrizeTable.userId eq userId }
            .map {
                NobelPrize(
                    id = it[PrizeTable.id],
                    awardYear = it[PrizeTable.awardYear],
                    category = it[PrizeTable.category],
                    fullName = it[PrizeTable.fullName],
                    motivation = it[PrizeTable.motivation]
                )
            }
    }

    suspend fun addFavorite(userId: Int, prizeId: Int): Boolean = newSuspendedTransaction {
        val exists = UserPrizeTable.selectAll()
            .where { (UserPrizeTable.userId eq userId) and (UserPrizeTable.prizeId eq prizeId) }
            .count() > 0
        if (!exists) {
            UserPrizeTable.insert {
                it[this.userId] = userId
                it[this.prizeId] = prizeId
                it[addedAt] = Instant.now()
            }
            true
        } else {
            false
        }
    }

    suspend fun removeFavorite(userId: Int, prizeId: Int): Int = newSuspendedTransaction {
        UserPrizeTable.deleteWhere {
            (UserPrizeTable.userId eq userId) and (UserPrizeTable.prizeId eq prizeId)
        }
    }
}
