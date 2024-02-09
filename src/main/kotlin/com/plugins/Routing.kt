package com.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Welcome To Prathamesh's World!")
        }

        get("/pratham"){
            call.respondText("Welcome To Pratham Page!")
        }
    }

    routing {
        route("/notes"){
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
        get ("/notes/{id}") {
            val id = call.parameters["id"]
            call.respond("$id")
        }

        get("/notes") {
            val id = call.request.queryParameters["id"]
            call.respond("$id")
        }
    }
}
