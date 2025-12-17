package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.uuid.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProductServiceTest {

    private val productRepository = mockk<ProductRepository>()
    private val productService = ProductService(productRepository)

    @Test
    fun `create product with valid data`() = runBlocking {
        // Given
        val productName = "Apple"
        val categoryId = Uuid.random()
        val expectedProduct = Product(name = productName, categoryId = categoryId)
        
        coEvery { productRepository.create(any(), any()) } returns expectedProduct

        // When
        val result = productService.create(productName, categoryId)

        // Then
        assertEquals(expectedProduct, result)
        coVerify { productRepository.create(productName, categoryId.toString()) }
    }

    @Test
    fun `read all products`() = runBlocking {
        // Given
        val expectedProducts = listOf(
            Product(name = "Apple", categoryId = Uuid.random()),
            Product(name = "Banana", categoryId = Uuid.random())
        )
        coEvery { productRepository.readAll() } returns expectedProducts

        // When
        val result = productService.readAll()

        // Then
        assertEquals(expectedProducts, result)
        coVerify { productRepository.readAll() }
    }

    @Test
    fun `read product by valid id`() = runBlocking {
        // Given
        val productId = Uuid.random()
        val expectedProduct = Product(
            id = productId, 
            name = "Apple", 
            categoryId = Uuid.random()
        )
        coEvery { productRepository.readById(productId.toString()) } returns expectedProduct

        // When
        val result = productService.readById(productId)

        // Then
        assertEquals(expectedProduct, result)
        coVerify { productRepository.readById(productId.toString()) }
    }

    @Test
    fun `return null when reading non-existent product`() = runBlocking {
        // Given
        val productId = Uuid.random()
        coEvery { productRepository.readById(productId.toString()) } returns null

        // When
        val result = productService.readById(productId)

        // Then
        assertNull(result)
        coVerify { productRepository.readById(productId.toString()) }
    }

    @Test
    fun `update existing product`() = runBlocking {
        // Given
        val productId = Uuid.random()
        val categoryId = Uuid.random()
        val updatedProduct = Product(
            id = productId, 
            name = "Updated Apple", 
            categoryId = categoryId
        )
        val existingProduct = Product(
            id = productId, 
            name = "Apple", 
            categoryId = categoryId
        )
        
        coEvery { productRepository.readById(productId.toString()) } returns existingProduct
        coEvery { productRepository.update(updatedProduct) } returns true

        // When
        val result = productService.update(updatedProduct)

        // Then
        assertEquals(updatedProduct, result)
        coVerify { 
            productRepository.readById(productId.toString())
            productRepository.update(updatedProduct) 
        }
    }

    @Test
    fun `return null when updating non-existent product`() = runBlocking {
        // Given
        val productId = Uuid.random()
        val product = Product(
            id = productId, 
            name = "Apple", 
            categoryId = Uuid.random()
        )
        coEvery { productRepository.readById(productId.toString()) } returns null

        // When
        val result = productService.update(product)

        // Then
        assertNull(result)
        coVerify { productRepository.readById(productId.toString()) }
        coVerify(exactly = 0) { productRepository.update(any()) }
    }

    @Test
    fun `delete existing product`() = runBlocking {
        // Given
        val productId = Uuid.random()
        coEvery { productRepository.delete(productId.toString()) } returns true

        // When
        val result = productService.delete(productId)

        // Then
        assertTrue(result)
        coVerify { productRepository.delete(productId.toString()) }
    }

    @Test
    fun `return false when deleting non-existent product`() = runBlocking {
        // Given
        val productId = Uuid.random()
        coEvery { productRepository.delete(productId.toString()) } returns false

        // When
        val result = productService.delete(productId)

        // Then
        assertTrue(!result)
        coVerify { productRepository.delete(productId.toString()) }
    }
}
