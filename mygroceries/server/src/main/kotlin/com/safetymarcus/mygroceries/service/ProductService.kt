package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.Product
import java.util.UUID

class ProductService(
    private val productRepository: ProductRepository = ProductRepository
) {
    fun create(name: String, categoryId: UUID): Product =
        productRepository.create(name, categoryId)

    fun readAll(): List<Product> = productRepository.readAll()

    fun readById(id: UUID): Product? = productRepository.readById(id)

    fun update(product: Product): Product? {
        val existingProduct = productRepository.readById(product.id) ?: return null
        val updatedProduct = existingProduct.copy(
            name = product.name,
            categoryId = product.categoryId
        )
        return if (productRepository.update(updatedProduct)) {
            updatedProduct
        } else {
            null
        }
    }

    fun delete(id: UUID): Boolean = productRepository.delete(id)
}
