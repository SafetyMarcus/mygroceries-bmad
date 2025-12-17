package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Product
import java.util.UUID

class ProductService(
    private val productRepository: ProductRepository = ProductRepository
) {
    fun create(name: String, categoryId: UUID) = productRepository.create(name, categoryId)

    fun readAll(): List<Product> = productRepository.readAll()

    fun readById(id: UUID): Product? = productRepository.readById(id)

    fun update(product: Product) = productRepository
        .update(product)
        .takeIf { it }
        ?.let { productRepository.readById(product.id!!) }

    fun delete(id: UUID) = productRepository.delete(id) > 0
}