package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import com.safetymarcus.mygroceries.service.CategoryService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.categoryRoutes(categoryService: CategoryService) {

    route("/categories") {
        post {
            val category = call.receive<NewCategory>()
            val newCategory = categoryService.create(category)
            call.respond(HttpStatusCode.Created, newCategory)
        }

        get {
            val categories = categoryService.readAll()
            call.respond(categories)
        }

        get("/{id}") {
            val id = call.getAndValidateUUID("id")
            val category = categoryService.readById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(category)
        }

        put("/{id}") {
            val id = call.getAndValidateUUID("id")
            val category = call.receive<Category>()
            if (category.id != id) {
                call.respond(HttpStatusCode.BadRequest, "Category id does not match path id")
                return@put
            }

            val updatedCategory = categoryService.update(category)
            updatedCategory?.let { call.respond(it) } ?: call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}") {
            val id = call.getAndValidateUUID("id")
            if (categoryService.delete(id)) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}

suspend fun ApplicationCall.getAndValidateUUID(name: String): UUID {
    val param = parameters[name]?.toString()
    return try {
        UUID.fromString(param) 
    } catch (e: Exception) {
        respond(HttpStatusCode.BadRequest, "Invalid UUID: $param") 
        throw e
    }
}
