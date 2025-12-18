package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.server.module
import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.db.*
import com.safetymarcus.mygroceries.model.*
import com.safetymarcus.mygroceries.service.LineItemService
import com.safetymarcus.mygroceries.service.OrderService
import com.safetymarcus.mygroceries.service.ProductService
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
import kotlin.uuid.*
import kotlin.time.Clock

class LineItemRoutesTest {

    private lateinit var testLineItem: LineItem
    private lateinit var testOrder: Order
    private lateinit var testProduct: Product
    private lateinit var testCategory: Category

    private fun withTestApplication(test: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        
        runBlocking {
            testOrder = client.post("/orders") {
                contentType(ContentType.Application.Json)
                setBody(NewOrder(date = Clock.System.now()))
            }.body<Order>()
            
            testCategory = client.post("/categories") {
                contentType(ContentType.Application.Json)
                setBody(NewCategory("Test Category"))
            }.body<Category>()
            
            testProduct = client.post("/products") {
                contentType(ContentType.Application.Json)
                setBody(NewProduct(
                    name = "Test Product",
                    categoryId = testCategory.id
                ))
            }.body<Product>()
            
            testLineItem = client.post("/orders/${testOrder.id}/lineitems") {
                contentType(ContentType.Application.Json)
                setBody(NewLineItem(
                    productId = testProduct.id!!,
                    quantity = 2.0,
                    cost = 399  // $3.99 in cents
                ))
            }.body<LineItem>()
        }
        
        test(client)
    }

    @AfterEach
    fun tearDown() = runBlocking {
        LineItemRepository.deleteAll()
        ProductRepository.deleteAll()
        OrderRepository.deleteAll()
        CategoryRepository.deleteAll()
        Database.close()
    }

    @Test
    fun `Create a line item for an order`() = withTestApplication { client ->
        val newLineItem = NewLineItem(
            productId = testProduct.id!!,
            quantity = 1.0,
            cost = 199  // $1.99 in cents
        )
        
        val response = client.post("/orders/${testOrder.id}/lineitems") {
            contentType(ContentType.Application.Json)
            setBody(newLineItem)
        }
        
        assertEquals(HttpStatusCode.Created, response.status)
        val createdLineItem = response.body<LineItem>()
        assertNotNull(createdLineItem.id)
        assertEquals(testOrder.id, createdLineItem.orderId)
        assertEquals(newLineItem.productId, createdLineItem.productId)
        assertEquals(newLineItem.quantity, createdLineItem.quantity)
        assertEquals(newLineItem.cost, createdLineItem.cost)
    }

    @Test
    fun `Get all line items for an order`() = withTestApplication { client ->
        val response = client.get("/orders/${testOrder.id}/lineitems")
        assertEquals(HttpStatusCode.OK, response.status)
        val lineItems = response.body<List<LineItem>>()
        assertEquals(1, lineItems.size)
        assertEquals(testLineItem.id!!, lineItems[0].id)
    }

    @Test
    fun `Get line item by id`() = withTestApplication { client ->
        val response = client.get("/lineitems/${testLineItem.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val fetchedLineItem = response.body<LineItem>()
        assertEquals(testLineItem, fetchedLineItem)
    }

    @Test
    fun `Update line item`() = withTestApplication { client ->
        val updatedLineItem = LineItem(
            id = testLineItem.id!!,
            orderId = testOrder.id!!,
            productId = testProduct.id!!,
            quantity = 3.0,
            cost = 499  // $4.99 in cents
        )
        
        val response = client.put("/lineitems/${testLineItem.id}") {
            contentType(ContentType.Application.Json)
            setBody(updatedLineItem)
        }
        
        assertEquals(HttpStatusCode.OK, response.status)
        val responseLineItem = response.body<LineItem>()
        assertEquals(3.0, responseLineItem.quantity)
        assertEquals(499, responseLineItem.cost)
    }

    @Test
    fun `Delete line item`() = withTestApplication { client ->
        val deleteResponse = client.delete("/lineitems/${testLineItem.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getByIdResponse = client.get("/lineitems/${testLineItem.id}")
        assertEquals(HttpStatusCode.NotFound, getByIdResponse.status)
    }

    @Test
    fun `Get line item with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.get("/lineitems/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Update line item with invalid id in path returns bad request`() = withTestApplication { client ->
        val response = client.put("/lineitems/invalid-uuid") {
            contentType(ContentType.Application.Json)
            setBody(NewLineItem(productId = testProduct.id!!, quantity = 1.0, cost = 199))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Update line item with mismatched ids returns bad request`() = withTestApplication { client ->
        val response = client.put("/lineitems/${testLineItem.id}") {
            contentType(ContentType.Application.Json)
            setBody(LineItem(
                id = Uuid.random(),
                orderId = testOrder.id!!,
                productId = testProduct.id!!,
                quantity = 1.0,
                cost = 199
            ))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Delete line item with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.delete("/lineitems/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Get non-existent line item returns not found`() = withTestApplication { client ->
        val nonExistentId = Uuid.random()
        val response = client.get("/lineitems/$nonExistentId")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `Update non-existent line item returns not found`() = withTestApplication { client ->
        val nonExistentId = Uuid.random()
        val response = client.put("/lineitems/$nonExistentId") {
            contentType(ContentType.Application.Json)
            setBody(LineItem(
                id = nonExistentId,
                orderId = testOrder.id!!,
                productId = testProduct.id!!,
                quantity = 1.0,
                cost = 199
            ))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `Get line items for non-existent order returns empty list`() = withTestApplication { client ->
        val nonExistentOrderId = Uuid.random()
        val response = client.get("/orders/$nonExistentOrderId/lineitems")
        assertEquals(HttpStatusCode.OK, response.status)
        val lineItems = response.body<List<LineItem>>()
        assertTrue(lineItems.isEmpty())
    }

    @Test
    fun `Create line item with non-existent order returns not found`() = withTestApplication { client ->
        val nonExistentOrderId = Uuid.random()
        val newLineItem = NewLineItem(
            productId = testProduct.id!!,
            quantity = 1.0,
            cost = 199
        )
        
        val response = client.post("/orders/$nonExistentOrderId/lineitems") {
            contentType(ContentType.Application.Json)
            setBody(newLineItem)
        }
        
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Create line item with non-existent product returns bad request`() = withTestApplication { client ->
        val nonExistentProductId = Uuid.random()
        val newLineItem = NewLineItem(
            productId = nonExistentProductId,
            quantity = 1.0,
            cost = 199
        )
        
        val response = client.post("/orders/${testOrder.id}/lineitems") {
            contentType(ContentType.Application.Json)
            setBody(newLineItem)
        }
        
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
