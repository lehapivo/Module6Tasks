package com.example.nobelserver.data.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object UserTable : Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 100).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val role = varchar("role", 50).default("user")
    override val primaryKey = PrimaryKey(id)
}

object PrizeTable : Table("prizes") {
    val id = integer("id").autoIncrement()
    val awardYear = varchar("award_year", 10)
    val category = varchar("category", 100)
    val fullName = varchar("full_name", 255)
    val motivation = text("motivation")
    val detailLink = varchar("detail_link", 500).default("")
    override val primaryKey = PrimaryKey(id)
}

object LaureateTable : Table("laureates") {
    val id = integer("id").autoIncrement()
    val prizeId = integer("prize_id").references(PrizeTable.id)
    val fullName = varchar("full_name", 255)
    val portion = varchar("portion", 10)
    val motivation = text("motivation")
    val portraitUrl = varchar("portrait_url", 500).nullable()
    override val primaryKey = PrimaryKey(id)
}

object UserPrizeTable : Table("user_prizes") {
    val userId = integer("user_id").references(UserTable.id)
    val prizeId = integer("prize_id").references(PrizeTable.id)
    val addedAt = timestamp("added_at")
    override val primaryKey = PrimaryKey(userId, prizeId)
}
