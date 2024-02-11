package com.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.data.model.User

class JWTService {
    private val issuer = "notes_server"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier : JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User) : String{
        return JWT.create()
            .withSubject("NoteAppAuth")
            .withIssuer(issuer)
            .withClaim("userId", user.userId)
            .sign(algorithm)
    }
}