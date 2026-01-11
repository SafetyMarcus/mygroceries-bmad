package com.safetymarcus.mygroceries.server

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.db.CategorySpendingRepository
import com.safetymarcus.mygroceries.db.LineItemRepository
import com.safetymarcus.mygroceries.db.OrderRepository
import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.*
import com.safetymarcus.mygroceries.routes.*
import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.service.*
import com.safetymarcus.mygroceries.validators.validate
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

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
    install(CORS) {
        allowHost("localhost:8080", schemes = listOf("http", "https"))
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }
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
    val spendingService = SpendingService(CategorySpendingRepository)

    validations()
    categories(categoryService)
    products(productService, categoryService)
    orders(orderService)
    lineItems(lineItemService, productService, orderService)
    spending(spendingService)
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

fun Application.lineItems(
    lineItemService: LineItemService,
    productService: ProductService,
    orderService: OrderService
) {
    routing {
        lineItemRoutes(lineItemService, productService, orderService)
    }
}

fun Application.spending(spendingService: SpendingService) {
    routing {
        spendingRoutes(spendingService)
    }
}
