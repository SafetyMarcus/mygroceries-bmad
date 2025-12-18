package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Product
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlin.uuid.*

class ProductService(
    private val productRepository: ProductRepository = ProductRepository
) {
    suspend fun create(name: String, categoryId: Uuid) = productRepository.create(name, categoryId.toJavaUuid())

    suspend fun readAll() = productRepository.readAll()

    suspend fun readById(id: Uuid) = productRepository.readById(id.toJavaUuid())

    suspend fun update(product: Product) = productRepository
        .update(product)
        .takeIf { it }
        ?.let { productRepository.readById(product.id!!.toJavaUuid()) }

    suspend fun delete(id: Uuid): Boolean = productRepository.delete(id.toJavaUuid())

    context(call: ApplicationCall)
    suspend fun validateProductExists(productId: Uuid) {
        if (readById(productId) == null) {
            call.respond(HttpStatusCode.BadRequest, "Product does not exist")
            return
        }
    }
}