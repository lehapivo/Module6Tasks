package com.example.nobelserver.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {
    const val SECRET = "my-super-secret-key-for-nobel-prize-server-32chars"
    const val ISSUER = "nobel-prize-server"
    const val AUDIENCE = "mobile-app"
    const val REALM = "Nobel Prize Server"
    private const val EXPIRATION_MS = 30 * 60 * 1000L // 30 minutes

    val verifier = JWT.require(Algorithm.HMAC256(SECRET))
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    fun generateToken(username: String, role: String): String =
        JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("username", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_MS))
            .sign(Algorithm.HMAC256(SECRET))
}
