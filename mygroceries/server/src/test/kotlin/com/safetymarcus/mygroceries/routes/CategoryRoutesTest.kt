package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.server.module
import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
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
import java.util.UUID

class CategoryRoutesTest {

    private val categoryService = CategoryService()

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
    fun tearDown() {
        CategoryRepository.deleteAll()
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
        categoryService.create(NewCategory(name = "Fruits"))
        val response = client.get("/categories")
        assertEquals(HttpStatusCode.OK, response.status)
        val categories = response.body<List<Category>>()
        assertEquals(1, categories.size)
        assertEquals("Fruits", categories[0].name)
    }

    @Test
    fun `Get category by id`() = withTestApplication { client ->
        val createdCategory = categoryService.create(NewCategory(name = "Fruits"))

        val response = client.get("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val fetchedCategory = response.body<Category>()
        assertEquals(createdCategory, fetchedCategory)
    }

    @Test
    fun `Update category`() = withTestApplication { client ->
        val createdCategory = categoryService.create(NewCategory(name = "Fruits"))

        val response = client.put("/categories/${createdCategory.id}") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = "Fresh Fruits"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val updatedCategory = response.body<Category>()
        assertEquals("Fresh Fruits", updatedCategory.name)
    }

    @Test
    fun `Delete category`() = withTestApplication { client ->
        val createdCategory = categoryService.create(NewCategory(name = "Fruits"))

        val deleteResponse = client.delete("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getByIdResponse = client.get("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.NotFound, getByIdResponse.status)
    }

    @Test
    fun `Get category by invalid id returns bad request`() = withTestApplication { client ->
        val response = client.get("/categories/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Update category with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.put("/categories") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = "Vegetables"))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `Delete category with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.delete("/categories")
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
        val createdCategory = categoryService.create(NewCategory(name = "Fruits"))

        val response = client.put("/categories/${createdCategory.id}") {
            contentType(ContentType.Application.Json)
            setBody(NewCategory(name = ""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
