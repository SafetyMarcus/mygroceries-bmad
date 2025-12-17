package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.ProductRepository
import com.safetymarcus.mygroceries.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.UUID
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
        val categoryId = UUID.randomUUID()
        val expectedProduct = Product(name = productName, categoryId = categoryId)
        
        coEvery { productRepository.create(any(), any()) } returns expectedProduct

        // When
        val result = productService.create(productName, categoryId)

        // Then
        assertEquals(expectedProduct, result)
        coVerify { productRepository.create(productName, categoryId) }
    }

    @Test
    fun `read all products`() = runBlocking {
        // Given
        val expectedProducts = listOf(
            Product(name = "Apple", categoryId = UUID.randomUUID()),
            Product(name = "Banana", categoryId = UUID.randomUUID())
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
        val productId = UUID.randomUUID()
        val expectedProduct = Product(
            id = productId, 
            name = "Apple", 
            categoryId = UUID.randomUUID()
        )
        coEvery { productRepository.readById(productId) } returns expectedProduct

        // When
        val result = productService.readById(productId)

        // Then
        assertEquals(expectedProduct, result)
        coVerify { productRepository.readById(productId) }
    }

    @Test
    fun `return null when reading non-existent product`() = runBlocking {
        // Given
        val productId = UUID.randomUUID()
        coEvery { productRepository.readById(productId) } returns null

        // When
        val result = productService.readById(productId)

        // Then
        assertNull(result)
        coVerify { productRepository.readById(productId) }
    }

    @Test
    fun `update existing product`() = runBlocking {
        // Given
        val productId = UUID.randomUUID()
        val categoryId = UUID.randomUUID()
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
        
        coEvery { productRepository.readById(productId) } returns existingProduct
        coEvery { productRepository.update(updatedProduct) } returns true

        // When
        val result = productService.update(updatedProduct)

        // Then
        assertEquals(updatedProduct, result)
        coVerify { 
            productRepository.readById(productId)
            productRepository.update(updatedProduct) 
        }
    }

    @Test
    fun `return null when updating non-existent product`() = runBlocking {
        // Given
        val productId = UUID.randomUUID()
        val product = Product(
            id = productId, 
            name = "Apple", 
            categoryId = UUID.randomUUID()
        )
        coEvery { productRepository.readById(productId) } returns null

        // When
        val result = productService.update(product)

        // Then
        assertNull(result)
        coVerify { productRepository.readById(productId) }
        coVerify(exactly = 0) { productRepository.update(any()) }
    }

    @Test
    fun `delete existing product`() = runBlocking {
        // Given
        val productId = UUID.randomUUID()
        coEvery { productRepository.delete(productId) } returns true

        // When
        val result = productService.delete(productId)

        // Then
        assertTrue(result)
        coVerify { productRepository.delete(productId) }
    }

    @Test
    fun `return false when deleting non-existent product`() = runBlocking {
        // Given
        val productId = UUID.randomUUID()
        coEvery { productRepository.delete(productId) } returns false

        // When
        val result = productService.delete(productId)

        // Then
        assertTrue(!result)
        coVerify { productRepository.delete(productId) }
    }
}
