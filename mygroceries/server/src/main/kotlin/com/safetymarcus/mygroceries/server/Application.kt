package com.safetymarcus.mygroceries.server

import com.safetymarcus.mygroceries.server.db.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("/health") {
                call.respondText("OK")
            }
        }
    }.start(wait = true)
}
