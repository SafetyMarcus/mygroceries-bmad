package com.safetymarcus.mygroceries.server

import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.routes.*
import com.safetymarcus.mygroceries.db.*
import com.safetymarcus.mygroceries.service.*
import com.safetymarcus.mygroceries.model.*
import com.safetymarcus.mygroceries.validators.*
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
    val orderService = OrderService(OrderRepository)
    val lineItemService = LineItemService(LineItemRepository)
    
    validations()
    categories(categoryService)
    products(productService, categoryService)
    orders(orderService)
    lineItems(lineItemService, productService, orderService)
    health()
}

fun Application.health() {
    routing {
        get("/health") {
            call.respondText("OK")
        }
    }
}

fun Application.validations() {
    install(RequestValidation) {
        validate<Category> { it.validate().result() }
        validate<NewCategory> { it.validate().result() }
        validate<Product> { it.validate().result() }
        validate<NewProduct> { it.validate().result() }
        validate<Order> { it.validate().result() }
        validate<NewOrder> { it.validate().result() }
        validate<LineItem> { it.validate().result() }
        validate<NewLineItem> { it.validate().result() }
    }
}

fun Application.categories(service: CategoryService) {
    routing {
        categoryRoutes(service)
    }
}

fun Application.products(productService: ProductService, categoryService: CategoryService) {
    routing {
        productRoutes(productService, categoryService)
    }
}

fun Application.orders(orderService: OrderService) {
    routing {
        orderRoutes(orderService)
    }
}

fun Application.lineItems(lineItemService: LineItemService, productService: ProductService, orderService: OrderService) {
    routing {
        lineItemRoutes(lineItemService, productService, orderService)
    }
}
