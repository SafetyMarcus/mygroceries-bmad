package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Product
import kotlin.uuid.*

class ProductService(
    private val productRepository: ProductRepository = ProductRepository
) {
    fun create(name: String, categoryId: Uuid) = productRepository.create(name, categoryId.toString())

    fun readAll(): List<Product> = productRepository.readAll()

    fun readById(id: Uuid): Product? = productRepository.readById(id.toString())

    fun update(product: Product): Product? = productRepository
        .update(product)
        .takeIf { it }
        ?.let { productRepository.readById(product.id.toString()) }

    fun delete(id: Uuid): Boolean = productRepository.delete(id.toString())
}