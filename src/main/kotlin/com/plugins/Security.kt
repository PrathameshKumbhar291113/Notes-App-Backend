package com.plugins

import com.auth.JWTService
import com.repository.UserRepository
import com.utils.JWT_DECLARATION
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import kotlin.collections.set

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)

    val database = UserRepository()

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = "jwt-audience"
    val jwtDomain = "https://jwt-provider-domain/"
    val jwtRealm = "ktor note app"
    val jwtSecret = "secret"
    authentication {
        jwt (JWT_DECLARATION){
            realm = jwtRealm
            verifier(JWTService().verifier)
            validate {
                val payload = it.payload
                val userId = payload.getClaim("userId").asString()
                val user = database.findUserByUserId(userId)
                user
            }
        }
    }

    /*verifier(
    JWT
        .require(Algorithm.HMAC256(jwtSecret))
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .build()
)*/
    /* validate { credential ->
              if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
          }*/
    /*routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }*/
}
