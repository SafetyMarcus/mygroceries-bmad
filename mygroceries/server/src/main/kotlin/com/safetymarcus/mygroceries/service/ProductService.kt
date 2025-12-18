package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Product
import kotlin.uuid.*

class ProductService(
    private val productRepository: ProductRepository = ProductRepository
) {
    fun create(name: String, categoryId: Uuid) = productRepository.create(name, categoryId.toString())

    fun readAll() = productRepository.readAll()

    fun readById(id: Uuid) = productRepository.readById(id.toString())

    fun update(product: Product) = productRepository
        .update(product)
        .takeIf { it }
        ?.let { productRepository.readById(product.id.toString()) }

    fun delete(id: Uuid): Boolean = productRepository.delete(id.toString())

    context(call: ApplicationCall)
    suspend fun validateProductExists(productId: Uuid) {
        if (readById(productId) == null) {
            call.respond(HttpStatusCode.BadRequest, "Product does not exist")
            return
        }
    }
}