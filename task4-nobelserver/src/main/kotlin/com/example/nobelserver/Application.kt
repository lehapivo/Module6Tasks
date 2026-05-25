package com.example.nobelserver

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
    configurePlugins()
    configureRouting()
}
