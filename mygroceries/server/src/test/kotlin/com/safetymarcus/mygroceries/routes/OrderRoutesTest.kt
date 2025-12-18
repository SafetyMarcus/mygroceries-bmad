package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.server.module
import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.model.NewOrder
import com.safetymarcus.mygroceries.db.OrderRepository
import kotlin.uuid.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.HttpClient
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.jupiter.api.AfterEach
import kotlinx.coroutines.runBlocking
import kotlin.time.Clock

class OrderRoutesTest {

    private lateinit var testOrder: Order

    private fun withTestApplication(test: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        // Seed with a test order
        testOrder = client.post("/orders") {
            contentType(ContentType.Application.Json)
            setBody(NewOrder(date = Clock.System.now()))
        }.body<Order>()
        test(client)
    }

    @AfterEach
    fun tearDown() = runBlocking {
        OrderRepository.deleteAll()
        Database.close()
    }

    @Test
    fun `Create an order`() = withTestApplication { client ->
        val newOrder = NewOrder(date = Clock.System.now())
        val response = client.post("/orders") {
            contentType(ContentType.Application.Json)
            setBody(newOrder)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val createdOrder = response.body<Order>()
        assertNotNull(createdOrder.id)
        assertEquals(newOrder.date, createdOrder.date)
    }

    @Test
    fun `Get all orders`() = withTestApplication { client ->
        val response = client.get("/orders")
        assertEquals(HttpStatusCode.OK, response.status)
        val orders = response.body<List<Order>>()
        assertEquals(1, orders.size)
        assertEquals(testOrder.id, orders[0].id)
    }

    @Test
    fun `Get order by id`() = withTestApplication { client ->
        val response = client.get("/orders/${testOrder.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val fetchedOrder = response.body<Order>()
        assertEquals(testOrder, fetchedOrder)
    }

    @Test
    fun `Update order`() = withTestApplication { client ->
        val updatedDate = Clock.System.now()
        val response = client.put("/orders/${testOrder.id}") {
            contentType(ContentType.Application.Json)
            setBody(Order(id = testOrder.id, date = updatedDate))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val updatedOrder = response.body<Order>()
        assertEquals(updatedDate, updatedOrder.date)
    }

    @Test
    fun `Delete order`() = withTestApplication { client ->
        val deleteResponse = client.delete("/orders/${testOrder.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getByIdResponse = client.get("/orders/${testOrder.id}")
        assertEquals(HttpStatusCode.NotFound, getByIdResponse.status)
    }

    @Test
    fun `Get order by invalid id returns bad request`() = withTestApplication { client ->
        val response = client.get("/orders/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Update order with invalid id in path returns bad request`() = withTestApplication { client ->
        val response = client.put("/orders/invalid-uuid") {
            contentType(ContentType.Application.Json)
            setBody(NewOrder(date = Clock.System.now()))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Update order with mismatched ids returns bad request`() = withTestApplication { client ->
        val response = client.put("/orders/${testOrder.id}") {
            contentType(ContentType.Application.Json)
            setBody(Order(id = Uuid.random(), date = Clock.System.now()))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Delete order with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.delete("/orders/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Get non-existent order returns not found`() = withTestApplication { client ->
        val nonExistentId = Uuid.random()
        val response = client.get("/orders/$nonExistentId")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `Update non-existent order returns not found`() = withTestApplication { client ->
        val nonExistentId = Uuid.random()
        val response = client.put("/orders/$nonExistentId") {
            contentType(ContentType.Application.Json)
            setBody(Order(id = nonExistentId, date = Clock.System.now()))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
