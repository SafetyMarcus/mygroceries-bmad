package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.server.module
import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Product
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.HttpClient
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*
import kotlin.uuid.*

class ProductRoutesTest {
    private lateinit var testCategory: Category
    private lateinit var testProduct: Product

    private fun withTestApplication(test: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        // Seed with a test category
        testCategory = client.post("/categories") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = "Test Category"))
        }.body<Category>()
        
        // Seed with a test product
        testProduct = client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(NewProduct(name = "Test Product", categoryId = testCategory.id))
        }.body<Product>()
        
        test(client)
    }

    @AfterTest
    fun tearDown() {
        ProductRepository.deleteAll()
        CategoryRepository.deleteAll()
        Database.close()
    }

    @Test
    fun `Create a product`() = withTestApplication { client ->
        val response = client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(NewProduct(name = "New Test Product", categoryId = testCategory.id))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val createdProduct = response.body<Product>()
        assertNotNull(createdProduct.id)
        assertEquals("New Test Product", createdProduct.name)
        assertEquals(testCategory.id, createdProduct.categoryId)
    }

    @Test
    fun `Create product with non-existent category returns not found`() = withTestApplication { client ->
        val nonExistentId = Uuid.random()
        val response = client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(NewProduct(name = "Test Product", categoryId = nonExistentId))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Get all products`() = withTestApplication { client ->
        // Create another product
        client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(NewProduct(name = "Another Test Product", categoryId = testCategory.id))
        }
        
        val response = client.get("/products")
        assertEquals(HttpStatusCode.OK, response.status)
        val products = response.body<List<Product>>()
        assertEquals(2, products.size)
    }
    
    @Test
    fun `Get product by id`() = withTestApplication { client ->
        val response = client.get("/products/${testProduct.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val fetchedProduct = response.body<Product>()
        assertEquals(testProduct, fetchedProduct)
    }
    
    @Test
    fun `Get product by invalid id returns bad request`() = withTestApplication { client ->
        val response = client.get("/products/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
    
    @Test
    fun `Get non-existent product returns not found`() = withTestApplication { client ->
        val nonExistentId = Uuid.random()
        val response = client.get("/products/$nonExistentId")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
    
    @Test
    fun `Update product`() = withTestApplication { client ->
        val updatedProduct = Product(
            id = testProduct.id,
            name = "Updated Test Product",
            categoryId = testProduct.categoryId
        )
        
        val response = client.put("/products/${testProduct.id}") {
            contentType(ContentType.Application.Json)
            setBody(updatedProduct)
        }
        
        assertEquals(HttpStatusCode.OK, response.status)
        val responseProduct = response.body<Product>()
        assertEquals("Updated Test Product", responseProduct.name)
    }
    
    @Test
    fun `Delete product`() = withTestApplication { client ->
        val deleteResponse = client.delete("/products/${testProduct.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getByIdResponse = client.get("/products/${testProduct.id}")
        assertEquals(HttpStatusCode.NotFound, getByIdResponse.status)
    }
    
    @Test
    fun `Update product with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.put("/products/invalid-uuid") {
            contentType(ContentType.Application.Json)
            setBody(NewProduct(name = "Test Product", categoryId = testCategory.id))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
    
    @Test
    fun `Update product with non-existent category returns not found`() = withTestApplication { client ->
        val nonExistentId = Uuid.random()
        val response = client.put("/products/${testProduct.id}") {
            contentType(ContentType.Application.Json)
            setBody(Product(id = testProduct.id, name = "Test Product", categoryId = nonExistentId))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
    
    @Test
    fun `Delete product with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.delete("/products/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
    
    @Test
    fun `Delete non-existent product returns not found`() = withTestApplication { client ->
        val nonExistentId = Uuid.random()
        val response = client.delete("/products/$nonExistentId")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
    
    @Test
    fun `Create product with empty name returns bad request`() = withTestApplication { client ->
        val response = client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(NewProduct(name = "", categoryId = testCategory.id))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
    
    @Test
    fun `Update product with empty name returns bad request`() = withTestApplication { client ->
        val response = client.put("/products/${testProduct.id}") {
            contentType(ContentType.Application.Json)
            setBody(Product(id = testProduct.id, name = "", categoryId = testCategory.id))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}