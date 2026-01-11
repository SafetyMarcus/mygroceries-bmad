package com.safetymarcus.mygroceries.server

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.db.LineItemRepository
import com.safetymarcus.mygroceries.db.OrderRepository
import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewCategory
import com.safetymarcus.mygroceries.model.NewLineItem
import com.safetymarcus.mygroceries.model.NewOrder
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.model.Product
import com.safetymarcus.mygroceries.routes.categoryRoutes
import com.safetymarcus.mygroceries.routes.lineItemRoutes
import com.safetymarcus.mygroceries.routes.orderRoutes
import com.safetymarcus.mygroceries.routes.productRoutes
import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.service.CategoryService
import com.safetymarcus.mygroceries.service.LineItemService
import com.safetymarcus.mygroceries.service.OrderService
import com.safetymarcus.mygroceries.service.ProductService
import com.safetymarcus.mygroceries.service.result
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
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error: ${cause.localizedMessage}")
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

fun Application.lineItems(
    lineItemService: LineItemService,
    productService: ProductService,
    orderService: OrderService
) {
    routing {
        lineItemRoutes(lineItemService, productService, orderService)
    }
}
