package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.service.CategoryService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.categoryRoutes(categoryService: CategoryService) {

    route("/categories") {
        post {
            val category = call.receive<Category>()
            val newCategory = categoryService.create(category)
            call.respond(HttpStatusCode.Created, newCategory)
        }

        get {
            val categories = categoryService.readAll()
            call.respond(categories)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id")
            val category = categoryService.readById(id)
            if (category != null) {
                call.respond(category)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing id")
            val category = call.receive<Category>()
            val updatedCategory = categoryService.update(id, category)
            if (updatedCategory != null) {
                call.respond(updatedCategory)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")
            val deleted = categoryService.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
