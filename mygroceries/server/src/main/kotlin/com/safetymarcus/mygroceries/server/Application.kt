package com.safetymarcus.mygroceries.server

import com.safetymarcus.mygroceries.server.db.DatabaseFactory
import com.safetymarcus.mygroceries.routes.*
import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.service.CategoryService
import com.safetymarcus.mygroceries.service.CategoryValidator
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.config.ApplicationConfigurationException
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import io.ktor.server.util.getValue
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(
        Netty,
        port = 8081,
        module = { module() },
    ).start(wait = true)
}

fun Application.configureDatabases() {
    DatabaseFactory.init()
}

fun Application.module() {
    configureDatabases()
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    categories()
    routing {
        get("/health") {
            call.respondText("OK")
        }
    }
}

fun Application.categories() {
    routing {
        categoryRoutes(CategoryService(CategoryRepository(), CategoryValidator()))
    }
}
