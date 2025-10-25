package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.db.TestDatabaseFactory
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.service.CategoryService
import io.ktor.client.* 
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CategoryRoutesTest {

    private val categoryService = CategoryService(CategoryRepository())

    @Before
    fun setup() {
        TestDatabaseFactory.init()
    }

    @After
    fun tearDown() {
        TestDatabaseFactory.close()
    }

    private fun withTestApplication(test: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) = testApplication {
        application {
            module(categoryService)
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
        categoryService.create(Category(id = "", name = "Fruits"))

        val response = client.get("/categories")
        assertEquals(HttpStatusCode.OK, response.status)
        val categories = response.body<List<Category>>()
        assertEquals(1, categories.size)
        assertEquals("Fruits", categories[0].name)
    }

    @Test
    fun `test get category by id`() = withTestApplication { client ->
        val createdCategory = categoryService.create(Category(id = "", name = "Fruits"))

        val response = client.get("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val fetchedCategory = response.body<Category>()
        assertEquals(createdCategory, fetchedCategory)
    }

    @Test
    fun `test update category`() = withTestApplication { client ->
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
        val createdCategory = categoryService.create(Category(id = "", name = "Fruits"))

        val deleteResponse = client.delete("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getByIdResponse = client.get("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.NotFound, getByIdResponse.status)
    }
}

fun Application.module(categoryService: CategoryService) {
    install(ServerContentNegotiation) {
        json()
    }
    routing {
        categoryRoutes(categoryService)
    }
}
