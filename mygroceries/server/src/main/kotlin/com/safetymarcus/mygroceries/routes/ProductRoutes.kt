package com.safetymarcus.mygroceries.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.uuid.*
import com.safetymarcus.mygroceries.routes.getAndValidateUUID
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Product
import com.safetymarcus.mygroceries.service.ProductService
import com.safetymarcus.mygroceries.service.CategoryService

context(call: ApplicationCall)
suspend fun CategoryService.validateCategoryExists(categoryId: Uuid) {
    val category = readById(categoryId)
    if (category == null) {
        call.respond(HttpStatusCode.BadRequest, "Category does not exist")
        return
    }
}

fun Route.productRoutes(productService: ProductService, categoryService: CategoryService) {
    route("/products") {
        post {
            val newProduct = call.receive<NewProduct>()

            context(call) {
                categoryService.validateCategoryExists(newProduct.categoryId!!)
            }

            val createdProduct = productService.create(
                name = newProduct.name,
                categoryId = newProduct.categoryId!!
            )
            call.respond(HttpStatusCode.Created, createdProduct)
        }

        get {
            val products = productService.readAll()
            call.respond(products)
        }

        get("/{id}") {
            val id = call.getAndValidateUUID("id")
            val product = productService.readById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(product)
        }

        put("/{id}") {
            val id = call.getAndValidateUUID("id")
            val updateProduct = call.receive<Product>()
            
            if (updateProduct.id != id) {
                call.respond(HttpStatusCode.BadRequest, "Product ID in path does not match ID in request body")
                return@put
            }

            context(call) {
                categoryService.validateCategoryExists(updateProduct.categoryId!!)
            }

            val updatedProduct = productService.update(updateProduct)
            updatedProduct?.let { call.respond(it) } ?: call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}") {
            val id = call.getAndValidateUUID("id")
            if (productService.delete(id)) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}
