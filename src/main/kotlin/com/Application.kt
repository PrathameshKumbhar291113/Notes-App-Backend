package com

import com.plugins.configureRouting
import com.plugins.configureSecurity
import com.plugins.configureSerialization
import com.repository.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.initializationOfDb()

    configureSerialization()
    configureSecurity()
    configureRouting()
}
