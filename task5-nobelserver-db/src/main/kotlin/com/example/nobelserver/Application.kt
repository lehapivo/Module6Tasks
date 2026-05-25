package com.example.nobelserver

import com.example.nobelserver.data.database.DatabaseFactory
import com.example.nobelserver.plugins.configurePlugins
import com.example.nobelserver.presentation.configureRouting
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // ⚠️ Replace with your actual neon.tech connection string
    // OR set environment variables: DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD
    val jdbcUrl = "jdbc:postgresql://ep-plain-wind-aqj022tk.c-8.us-east-1.aws.neon.tech/neondb?sslmode=require"
    val dbUser = "neondb_owner"
    val dbPassword = "npg_mFp1ATZXH3fj"

    DatabaseFactory.init(jdbcUrl, dbUser, dbPassword)
    configurePlugins()
    configureRouting()
}
