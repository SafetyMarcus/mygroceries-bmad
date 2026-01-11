package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.db.LineItemRepository
import com.safetymarcus.mygroceries.db.OrderRepository
import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.*
import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.server.module
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

class SpendingRoutesTest {

    private fun withTestApplication(test: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        test(client)
    }

    @AfterTest
    fun tearDown() = runBlocking {
        LineItemRepository.deleteAll()
        OrderRepository.deleteAll()
        ProductRepository.deleteAll()
        CategoryRepository.deleteAll()
        Database.close()
    }

    @Test
    fun `Get category spending returns calculated totals`() = withTestApplication { client ->
        // 1. Create a category
        val category = client.post("/categories") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = "Groceries"))
        }.body<Category>()

        // 2. Create a product
        val product = client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(NewProduct(name = "Milk", categoryId = category.id!!))
        }.body<Product>()

        // 3. Create an order
        val order = client.post("/orders") {
            contentType(ContentType.Application.Json)
            setBody(NewOrder(date = Clock.System.now()))
        }.body<Order>()

        // 4. Create a line item for that order
        client.post("/orders/${order.id}/lineitems") {
            contentType(ContentType.Application.Json)
            setBody(NewLineItem(productId = product.id!!, quantity = 2.0, cost = 250))
        }

        // 5. Get spending
        val response = client.get("/spending/categories")
        assertEquals(HttpStatusCode.OK, response.status)
        
        val spending = response.body<List<CategorySpending>>()
        assertTrue(spending.any { it.categoryName == "Groceries" && it.totalSpend == 2.50 })
    }
}
