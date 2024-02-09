package com.plugins

import com.auth.JWTService
import com.auth.hash
import com.data.model.User
import com.repository.Repository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val db = Repository()
    val jwtService = JWTService()
    val hashFunc = { s: String -> hash(s) }


    routing {
        get("/") {
            call.respondText("Welcome To Prathamesh's World!")
        }

        get("/pratham") {
            call.respondText("Welcome To Pratham Page!")
        }
    }

    routing {
        route("/notes") {
//            route("/create") {
            post {
                val body = call.receive<String>()
                call.respond("$body is successfully created")
            }
//            }

            delete {
                val body = call.receive<String>()
                call.respond("$body is success fully deleted")
            }
        }

    }

    routing {
        get("/notes/{id}") {
            val id = call.parameters["id"]
            call.respond("$id")
        }

        get("/notes") {
            val id = call.request.queryParameters["id"]
            call.respond("$id")
        }
    }

    routing {
        route("/add_user") {

        }

        route("/get_token") {
            get {
                val userName = call.request.queryParameters["name"]
                val userEmail = call.request.queryParameters["email"]
                val userPassword = call.request.queryParameters["password"]

                userName?.let {
                    userEmail?.let {
                        userPassword?.let {
                            val user = User(0,userName, userEmail, hashFunc(userPassword))
                            call.respond(jwtService.generateToken(user))
                        }
                    }
                }
            }
        }
    }
}
