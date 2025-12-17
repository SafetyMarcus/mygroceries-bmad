package com.safetymarcus.mygroceries.server

import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.routes.*
import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.service.CategoryService
import com.safetymarcus.mygroceries.service.ProductService
import com.safetymarcus.mygroceries.service.validate
import com.safetymarcus.mygroceries.service.validate as validateProduct
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Product
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.cio.Request
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.config.ApplicationConfigurationException
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import io.ktor.server.util.getValue

fun main() {
    embeddedServer(
        Netty,
        port = 8081,
        module = { module() },
    ).start(wait = true)
}

fun Application.configureDatabases() {
    Database.init()
}

fun Application.module() {
    configureDatabases()
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
    }
    
    val categoryService = CategoryService(CategoryRepository)
    val productService = ProductService(ProductRepository)
    
    categories(categoryService)
    products(productService)
    health()
}

fun Application.health() {
    routing {
        get("/health") {
            call.respondText("OK")
        }
    }
}

fun Application.categories(service: CategoryService) {
    install(RequestValidation) {
        validate<Category> { it.validate() }
        validate<NewCategory> { it.validate() }
    }

    routing {
        categoryRoutes(service)
    }
}

fun Application.products(productService: ProductService) {
    install(RequestValidation) {
        validate<Product> { it.validate() }
        validate<NewProduct> { it.validate() }
    }

    routing {
        productRoutes(productService)
    }
}
