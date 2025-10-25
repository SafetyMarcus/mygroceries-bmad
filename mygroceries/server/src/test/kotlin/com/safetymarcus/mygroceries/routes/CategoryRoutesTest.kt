package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.server.module
import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.db.TestDatabaseFactory
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.service.CategoryService
import com.safetymarcus.mygroceries.service.CategoryValidator
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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CategoryRoutesTest {

    @BeforeEach
    fun setup() {
        TestDatabaseFactory.init()
    }

    @AfterEach
    fun tearDown() {
        TestDatabaseFactory.close()
    }

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

    @Test
    fun `test create category`() = withTestApplication { client ->
        val response = client.post("/categories") {
            contentType(ContentType.Application.Json)
            setBody(Category(id = "", name = "Fruits"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val createdCategory = response.body<Category>()
        assertNotNull(createdCategory.id)
        assertEquals("Fruits", createdCategory.name)
    }

    @Test
    fun `test get all categories`() = withTestApplication { client ->
        // Create a category using the service within the test context
        val categoryRepository = CategoryRepository()
        val categoryValidator = CategoryValidator()
        val categoryService = CategoryService(categoryRepository, categoryValidator)
        val createdCategory = categoryService.create(Category(id = "", name = "Fruits"))

        val response = client.get("/categories")
        assertEquals(HttpStatusCode.OK, response.status)
        val categories = response.body<List<Category>>()
        assertEquals(1, categories.size)
        assertEquals("Fruits", categories[0].name)
    }

    @Test
    fun `test get category by id`() = withTestApplication { client ->
        // Create a category using the service within the test context
        val categoryRepository = CategoryRepository()
        val categoryValidator = CategoryValidator()
        val categoryService = CategoryService(categoryRepository, categoryValidator)
        val createdCategory = categoryService.create(Category(id = "", name = "Fruits"))

        val response = client.get("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val fetchedCategory = response.body<Category>()
        assertEquals(createdCategory, fetchedCategory)
    }

    @Test
    fun `test update category`() = withTestApplication { client ->
        // Create a category using the service within the test context
        val categoryRepository = CategoryRepository()
        val categoryValidator = CategoryValidator()
        val categoryService = CategoryService(categoryRepository, categoryValidator)
        val createdCategory = categoryService.create(Category(id = "", name = "Fruits"))

        val response = client.put("/categories/${createdCategory.id}") {
            contentType(ContentType.Application.Json)
            setBody(Category(id = createdCategory.id, name = "Fresh Fruits"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val updatedCategory = response.body<Category>()
        assertEquals("Fresh Fruits", updatedCategory.name)
    }

    @Test
    fun `test delete category`() = withTestApplication { client ->
        // Create a category using the service within the test context
        val categoryRepository = CategoryRepository()
        val categoryValidator = CategoryValidator()
        val categoryService = CategoryService(categoryRepository, categoryValidator)
        val createdCategory = categoryService.create(Category(id = "", name = "Fruits"))

        val deleteResponse = client.delete("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getByIdResponse = client.get("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.NotFound, getByIdResponse.status)
    }

    @Test
    fun `test get category by invalid id returns bad request`() = withTestApplication { client ->
        val response = client.get("/categories/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test update category with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.put("/categories/invalid-uuid") {
            contentType(ContentType.Application.Json)
            setBody(Category(id = "", name = "Vegetables"))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test delete category with invalid id returns bad request`() = withTestApplication { client ->
        val response = client.delete("/categories/invalid-uuid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test create category with empty name returns bad request`() = withTestApplication { client ->
        val response = client.post("/categories") {
            contentType(ContentType.Application.Json)
            setBody(Category(id = "", name = ""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `test update category with empty name returns bad request`() = withTestApplication { client ->
        // Create a category using the service within the test context
        val categoryRepository = CategoryRepository()
        val categoryValidator = CategoryValidator()
        val categoryService = CategoryService(categoryRepository, categoryValidator)
        val createdCategory = categoryService.create(Category(id = "", name = "Fruits"))

        val response = client.put("/categories/${createdCategory.id}") {
            contentType(ContentType.Application.Json)
            setBody(Category(id = createdCategory.id, name = ""))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
