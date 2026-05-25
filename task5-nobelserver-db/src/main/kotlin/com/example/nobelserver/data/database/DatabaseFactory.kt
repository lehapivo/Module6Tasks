package com.example.nobelserver.data.database

import at.favre.lib.crypto.bcrypt.BCrypt
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(jdbcUrl: String, user: String, password: String) {
        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            driverClassName = "org.postgresql.Driver"
            username = user
            this.password = password
            maximumPoolSize = 10
            minimumIdle = 2
            idleTimeout = 300_000
            maxLifetime = 1_800_000
            connectionTimeout = 30_000
            // Required for neon.tech
            addDataSourceProperty("ssl", "true")
            addDataSourceProperty("sslmode", "require")
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(UserTable, PrizeTable, LaureateTable, UserPrizeTable)
            seedIfEmpty()
        }
    }

    private fun seedIfEmpty() {
        if (UserTable.selectAll().count() == 0L) {
            val hash = BCrypt.withDefaults().hashToString(12, "admin123".toCharArray())
            UserTable.insert {
                it[username] = "admin"
                it[passwordHash] = hash
                it[role] = "admin"
            }
            val userHash = BCrypt.withDefaults().hashToString(12, "user123".toCharArray())
            UserTable.insert {
                it[username] = "user"
                it[passwordHash] = userHash
                it[role] = "user"
            }
        }

        if (PrizeTable.selectAll().count() == 0L) {
            // Seed some Nobel Prizes
            val prizeId = PrizeTable.insert {
                it[awardYear] = "2023"
                it[category] = "physics"
                it[fullName] = "Pierre Agostini, Ferenc Krausz, Anne L'Huillier"
                it[motivation] = "for experimental methods that generate attosecond pulses of light"
            } get PrizeTable.id

            LaureateTable.insert {
                it[this.prizeId] = prizeId
                it[fullName] = "Pierre Agostini"
                it[portion] = "1/3"
                it[motivation] = "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter"
            }
            LaureateTable.insert {
                it[this.prizeId] = prizeId
                it[fullName] = "Ferenc Krausz"
                it[portion] = "1/3"
                it[motivation] = "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter"
            }
            LaureateTable.insert {
                it[this.prizeId] = prizeId
                it[fullName] = "Anne L'Huillier"
                it[portion] = "1/3"
                it[motivation] = "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter"
            }
        }
    }
}
