package com.example.raicesvivas

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("PORT")?.toInt() ?: 8080
    ) {
        install(ContentNegotiation) { json() }
        routing {
            get("/") {
                call.respond(mapOf("status" to "ok"))
            }
            get("/registro") {
                call.respond(mapOf("status" to "ok", "data" to "Registro funcionando"))
            }
        }
    }.start(wait = true)
}
