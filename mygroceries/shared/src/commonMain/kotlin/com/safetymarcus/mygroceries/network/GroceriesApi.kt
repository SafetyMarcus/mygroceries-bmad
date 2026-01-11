package com.safetymarcus.mygroceries.network

import com.safetymarcus.mygroceries.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.uuid.Uuid

class GroceriesApi(private val client: HttpClient) {

    // Category Endpoints
    suspend fun getCategories(): List<Category> = client.get("categories").body()
    suspend fun getCategory(id: Uuid): Category = client.get("categories/$id").body()
    suspend fun createCategory(category: NewCategory): Category = client.post("categories") {
        setBody(category)
        contentType(ContentType.Application.Json)
    }.body()
    suspend fun updateCategory(id: Uuid, category: Category): Category = client.put("categories/$id") {
        setBody(category)
        contentType(ContentType.Application.Json)
    }.body()
    suspend fun deleteCategory(id: Uuid) = client.delete("categories/$id")

    // Product Endpoints
    suspend fun getProducts(): List<Product> = client.get("products").body()
    suspend fun getProduct(id: Uuid): Product = client.get("products/$id").body()
    suspend fun createProduct(product: NewProduct): Product = client.post("products") {
        setBody(product)
        contentType(ContentType.Application.Json)
    }.body()
    suspend fun updateProduct(id: Uuid, product: Product): Product = client.put("products/$id") {
        setBody(product)
        contentType(ContentType.Application.Json)
    }.body()
    suspend fun deleteProduct(id: Uuid) = client.delete("products/$id")

    // Order Endpoints
    suspend fun getOrders(): List<Order> = client.get("orders").body()
    suspend fun getOrder(id: Uuid): Order = client.get("orders/$id").body()
    suspend fun createOrder(order: NewOrder): Order = client.post("orders") {
        setBody(order)
        contentType(ContentType.Application.Json)
    }.body()
    suspend fun updateOrder(id: Uuid, order: Order): Order = client.put("orders/$id") {
        setBody(order)
        contentType(ContentType.Application.Json)
    }.body()
    suspend fun deleteOrder(id: Uuid) = client.delete("orders/$id")

    // Line Item Endpoints
    suspend fun getLineItems(orderId: Uuid): List<LineItem> = client.get("orders/$orderId/lineitems").body()
    suspend fun createLineItem(orderId: Uuid, lineItem: NewLineItem): LineItem = client.post("orders/$orderId/lineitems") {
        setBody(lineItem)
        contentType(ContentType.Application.Json)
    }.body()

    // Spending Endpoints
    suspend fun getCategorySpending(): List<CategorySpending> = client.get("spending/categories").body()

    companion object {
        fun createClient(baseUrl: String): HttpClient {
            return HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    })
                }
                install(Logging) {
                    level = LogLevel.ALL
                    logger = Logger.DEFAULT
                }
                install(DefaultRequest) {
                    url(baseUrl)
                }
                install(HttpRequestRetry) {
                    retryOnExceptionOrServerErrors(maxRetries = 3)
                    exponentialDelay()
                }
            }
        }
    }
}
