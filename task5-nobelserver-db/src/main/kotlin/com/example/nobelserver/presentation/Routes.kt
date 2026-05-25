package com.example.nobelserver.presentation

import com.example.nobelserver.data.repository.NobelRepositoryImpl
import com.example.nobelserver.data.repository.UserPrizeRepositoryImpl
import com.example.nobelserver.data.repository.UserRepositoryImpl
import com.example.nobelserver.security.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val username: String, val role: String)

fun Application.configureRouting() {
    val nobelRepo = NobelRepositoryImpl()
    val userRepo = UserRepositoryImpl()
    val userPrizeRepo = UserPrizeRepositoryImpl()

    routing {
        get("/") {
            call.respond(
                mapOf(
                    "status" to "Nobel Prize API v2.0 (PostgreSQL)",
                    "endpoints" to listOf(
                        "POST /login",
                        "GET /prizes",
                        "GET /users/me [JWT]",
                        "GET /users/me/prizes [JWT]",
                        "POST /users/me/prizes/{id} [JWT]",
                        "DELETE /users/me/prizes/{id} [JWT]"
                    )
                )
            )
        }

        // POST /login — get JWT token
        post("/login") {
            val req = call.receive<LoginRequest>()
            val user = userRepo.findByUsername(req.username)
            if (user == null || !userRepo.verifyPassword(req.password, user.passwordHash)) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Invalid username or password")
                )
                return@post
            }
            val token = JwtConfig.generateToken(user.username, user.role, user.id)
            call.respond(LoginResponse(token, user.username, user.role))
        }

        // GET /prizes — public, no auth required
        get("/prizes") {
            call.respond(nobelRepo.getAllPrizes())
        }

        // GET /prizes/{id} — public
        get("/prizes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid prize ID"))
            val prize = nobelRepo.getPrizeById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound, mapOf("error" to "Prize not found"))
            call.respond(prize)
        }

        // All /users/me routes require JWT
        authenticate("auth-jwt") {
            // GET /users/me — returns current user info from token
            get("/users/me") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString()
                val role = principal?.payload?.getClaim("role")?.asString()
                call.respond(mapOf("username" to username, "role" to role))
            }

            // GET /users/me/prizes — user's favourite prizes
            get("/users/me/prizes") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                call.respond(userPrizeRepo.getFavorites(userId))
            }

            // POST /users/me/prizes/{prizeId} — add to favourites
            post("/users/me/prizes/{prizeId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val prizeId = call.parameters["prizeId"]?.toIntOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid prize ID"))
                val added = userPrizeRepo.addFavorite(userId, prizeId)
                if (added) {
                    call.respond(HttpStatusCode.Created, mapOf("message" to "Prize added to favourites"))
                } else {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Already in favourites"))
                }
            }

            // DELETE /users/me/prizes/{prizeId} — remove from favourites
            delete("/users/me/prizes/{prizeId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt()
                    ?: return@delete call.respond(HttpStatusCode.Unauthorized)
                val prizeId = call.parameters["prizeId"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid prize ID"))
                val deleted = userPrizeRepo.removeFavorite(userId, prizeId)
                if (deleted > 0) {
                    call.respond(mapOf("message" to "Prize removed from favourites"))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Prize not in favourites"))
                }
            }
        }
    }
}
