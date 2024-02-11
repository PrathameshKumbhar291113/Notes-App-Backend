package com.plugins

import com.auth.JWTService
import com.auth.hash
import com.repository.NotesRepository
import com.repository.UserRepository
import com.routes.notesRoutes
import com.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        val userDatabase = UserRepository()
        val notesDatabase = NotesRepository()
        val jwtService = JWTService()
        val hashFunc = { s: String -> hash(s) }

        userRoutes(userDatabase, jwtService, hashFunc)
        notesRoutes(notesDatabase, hashFunc)
    }

    /*
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
            route("/create") {
                post {
                    val body = call.receive<String>()
                    call.respond("$body is successfully created")
                }
            }

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
    }*/

    /* routing {
        route("/add_user") {
            post {
                val parameters = call.receive<Parameters>()
                var userId : String = ""
                val userName = parameters["name"]
                val userEmail = parameters["email"]
                val userPassword = parameters["password"]

                do {
                    userId = (100000..999999).random().toString() // Generate a 6-digit random integer
                } while (db.isUserExists(userId)) // Check if the generated userId already exists

                userName?.let {
                    userEmail?.let {
                        userPassword?.let {
                            val user = User(userId,userName, userEmail, hashFunc(userPassword))
                            db.addUser(user)
                            call.respond(jwtService.generateToken(user)+"\n user generated successfully for $user")
                        }
                    }
                }
            }
        }

        route("/get_token") {
            get {
                val userName = call.request.queryParameters["name"]
                val userEmail = call.request.queryParameters["email"]
                val userPassword = call.request.queryParameters["password"]

                userName?.let {
                    userEmail?.let {
                        userPassword?.let {
                            val user = User("",userName, userEmail, hashFunc(userPassword))
                            call.respond(jwtService.generateToken(user))
                        }
                    }
                }
            }
        }
    }*/
}
