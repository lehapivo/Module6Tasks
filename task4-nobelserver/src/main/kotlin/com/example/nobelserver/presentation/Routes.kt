package com.example.nobelserver.presentation

import com.example.nobelserver.di.AppModule
import com.example.nobelserver.security.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val username: String, val role: String)

fun Application.configureRouting() {
    val nobelRepo = AppModule.nobelRepository
    val userRepo = AppModule.userRepository

    routing {
        get("/") {
            call.respond(mapOf("status" to "Nobel Prize API v1.0", "docs" to "/prizes"))
        }

        // POST /auth/login
        post("/auth/login") {
            val req = call.receive<LoginRequest>()
            val user = userRepo.findByUsername(req.username)
            if (user == null || user.passwordHash != req.password) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
                return@post
            }
            val token = JwtConfig.generateToken(user.username, user.role)
            call.respond(LoginResponse(token, user.username, user.role))
        }

        // Protected routes
        authenticate("auth-jwt") {
            // GET /prizes
            get("/prizes") {
                call.respond(nobelRepo.getAllPrizes())
            }

            // GET /prizes/{year}/{category}
            get("/prizes/{year}/{category}") {
                val year = call.parameters["year"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val category = call.parameters["category"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val prize = nobelRepo.getPrize(year, category)
                    ?: return@get call.respond(HttpStatusCode.NotFound, mapOf("error" to "Prize not found"))
                call.respond(prize)
            }

            // GET /prizes/{year}/{category}/laureates
            get("/prizes/{year}/{category}/laureates") {
                val year = call.parameters["year"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val category = call.parameters["category"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val laureates = nobelRepo.getLaureates(year, category)
                    ?: return@get call.respond(HttpStatusCode.NotFound, mapOf("error" to "Prize not found"))
                call.respond(laureates)
            }
        }
    }
}
