package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.db.TestDatabaseFactory
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.service.CategoryService
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CategoryRoutesTest {

    @Test
    fun `test category crud operations`() = testApplication {
        TestDatabaseFactory.init()
        val categoryService = CategoryService(CategoryRepository())
        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                categoryRoutes(categoryService)
            }
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // Create
        val createResponse = client.post("/categories") {
            contentType(ContentType.Application.Json)
            setBody(Category(id = "", name = "Fruits"))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        val createdCategory = createResponse.body<Category>()
        assertNotNull(createdCategory.id)
        assertEquals("Fruits", createdCategory.name)

        // Read all
        val getAllResponse = client.get("/categories")
        assertEquals(HttpStatusCode.OK, getAllResponse.status)
        val categories = getAllResponse.body<List<Category>>()
        assertEquals(1, categories.size)
        assertEquals(createdCategory, categories[0])

        // Read by id
        val getByIdResponse = client.get("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.OK, getByIdResponse.status)
        val fetchedCategory = getByIdResponse.body<Category>()
        assertEquals(createdCategory, fetchedCategory)

        // Update
        val updateResponse = client.put("/categories/${createdCategory.id}") {
            contentType(ContentType.Application.Json)
            setBody(Category(id = createdCategory.id, name = "Fresh Fruits"))
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        val updatedCategory = updateResponse.body<Category>()
        assertEquals("Fresh Fruits", updatedCategory.name)

        // Delete
        val deleteResponse = client.delete("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        // Verify deletion
        val getByIdAfterDeleteResponse = client.get("/categories/${createdCategory.id}")
        assertEquals(HttpStatusCode.NotFound, getByIdAfterDeleteResponse.status)
    }
}
