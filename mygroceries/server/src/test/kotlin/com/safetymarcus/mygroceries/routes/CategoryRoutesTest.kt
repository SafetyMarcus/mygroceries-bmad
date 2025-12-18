package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.server.module
import com.safetymarcus.mygroceries.server.db.Database
import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import com.safetymarcus.mygroceries.model.Product
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.db.ProductRepository
import kotlin.uuid.*
import com.safetymarcus.mygroceries.service.CategoryService
import com.safetymarcus.mygroceries.routes.categoryRoutes
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.HttpClient
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.server.application.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlin.uuid.*

class CategoryRoutesTest {

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
        //Seed with a test category
        testCategory = client.post("/categories") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = "Test"))
        }.body<Category>()
        test(client)
    }

    @AfterTest
    fun tearDown() = runBlocking {
        CategoryRepository.deleteAll()
        Database.close()
    }

    @Test
    fun `Create a category`() = withTestApplication { client ->
        val response = client.post("/categories") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = "Fruits"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val createdCategory = response.body<Category>()
        assertNotNull(createdCategory.id)
        assertEquals("Fruits", createdCategory.name)
    }

    @Test
    fun `Get all categories`() = withTestApplication { client ->
        val response = client.get("/categories")
        assertEquals(HttpStatusCode.OK, response.status)
        val categories = response.body<List<Category>>()
        assertEquals(1, categories.size)
        assertEquals("Test", categories[0].name)
    }

    @Test
    fun `Get category by id`() = withTestApplication { client ->
        val response = client.get("/categories/${testCategory.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val fetchedCategory = response.body<Category>()
        assertEquals(testCategory, fetchedCategory)
    }

    @Test
    fun `Update category`() = withTestApplication { client ->
        val response = client.put("/categories/${testCategory.id}") {
            contentType(ContentType.Application.Json)
            setBody(Category(id = testCategory.id, name = "Fresh Fruits"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val updatedCategory = response.body<Category>()
        assertEquals("Fresh Fruits", updatedCategory.name)
    }

    @Test
    fun `Delete category`() = withTestApplication { client ->
        val deleteResponse = client.delete("/categories/${testCategory.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getByIdResponse = client.get("/categories/${testCategory.id}")
        assertEquals(HttpStatusCode.NotFound, getByIdResponse.status)
    }

    @Test
    fun `Delete category with products cascades to products`() = withTestApplication { client ->
        // Create a product in the test category
        val newProduct = NewProduct(
            name = "Test Product",
            categoryId = Uuid.parse(testCategory.id.toString())
        )
        val productResponse = client.post("/products") {
            contentType(ContentType.Application.Json)
            setBody(newProduct)
        }
        assertEquals(HttpStatusCode.Created, productResponse.status)
        val createdProduct = productResponse.body<Product>()

        // Delete the category
        val deleteResponse = client.delete("/categories/${testCategory.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        // Verify the category is deleted
        val getCategoryResponse = client.get("/categories/${testCategory.id}")
        assertEquals(HttpStatusCode.NotFound, getCategoryResponse.status)

        // Verify the product is also deleted
        val getProductResponse = client.get("/products/${createdProduct.id}")
        assertEquals(HttpStatusCode.NotFound, getProductResponse.status)
    }

    @Test
    fun `Get category by invalid id returns bad request`() = withTestApplication { client ->
        val response = client.get("/categories/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Update category with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.put("/categories/invalid-uuid") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = "Vegetables"))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Delete category with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.delete("/categories/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Create category with empty name returns bad request`() = withTestApplication { client ->
        val response = client.post("/categories") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = ""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Update category with empty name returns bad request`() = withTestApplication { client ->
        val response = client.put("/categories/${Uuid.random()}") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = ""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
