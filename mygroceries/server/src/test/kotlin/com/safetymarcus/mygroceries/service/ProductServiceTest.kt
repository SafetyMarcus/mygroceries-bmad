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
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.toJavaUuid
import kotlinx.coroutines.runBlocking

class ProductServiceTest {

    private val productRepository = mockk<ProductRepository>()
    private val productService = ProductService(productRepository)

    @Test
    fun `create product with valid data`() = runBlocking {
        val productName = "Apple"
        val categoryId = Uuid.random()
        val expectedProduct = Product(name = productName, categoryId = categoryId)
        
        coEvery { productRepository.create(any(), any()) } returns expectedProduct

        val result = productService.create(productName, categoryId)

        assertEquals(expectedProduct, result)
        coVerify { productRepository.create(productName, categoryId.toJavaUuid()) }
    }

    @Test
    fun `read all products`() = runBlocking {
        val expectedProducts = listOf(
            Product(name = "Apple", categoryId = Uuid.random()),
            Product(name = "Banana", categoryId = Uuid.random())
        )
        coEvery { productRepository.readAll() } returns expectedProducts

        val result = productService.readAll()

        assertEquals(expectedProducts, result)
        coVerify { productRepository.readAll() }
    }

    @Test
    fun `read product by valid id`() = runBlocking {
        val productId = Uuid.random()
        val expectedProduct = Product(
            id = productId, 
            name = "Apple", 
            categoryId = Uuid.random()
        )
        coEvery { productRepository.readById(productId.toJavaUuid()) } returns expectedProduct

        val result = productService.readById(productId)

        assertEquals(expectedProduct, result)
        coVerify { productRepository.readById(productId.toJavaUuid()) }
    }

    @Test
    fun `return null when reading non-existent product`() = runBlocking {
        val productId = Uuid.random()
        coEvery { productRepository.readById(productId.toJavaUuid()) } returns null

        val result = productService.readById(productId)

        assertNull(result)
        coVerify { productRepository.readById(productId.toJavaUuid()) }
    }

    @Test
    fun `update existing product`() = runBlocking {
        val productId = Uuid.random()
        val updatedProduct = Product(
            id = productId, 
            name = "Updated Apple", 
            categoryId = Uuid.random()
        )
        
        coEvery { productRepository.update(updatedProduct) } returns true
        coEvery { productRepository.readById(productId.toJavaUuid()) } returns updatedProduct
        
        assertEquals(updatedProduct, productService.update(updatedProduct))
        coVerify { productRepository.update(updatedProduct) }
    }

    @Test
    fun `return null when updating non-existent product`() = runBlocking {
        val productId = Uuid.random()
        val product = Product(
            id = productId, 
            name = "Apple", 
            categoryId = Uuid.random()
        )
        coEvery { productRepository.update(product) } returns false

        assertNull(productService.update(product))
        coVerify { productRepository.update(product) }
    }

    @Test
    fun `delete existing product`() = runBlocking {
        val productId = Uuid.random()
        coEvery { productRepository.delete(productId.toJavaUuid()) } returns true

        val result = productService.delete(productId)

        assertTrue(result)
        coVerify { productRepository.delete(productId.toJavaUuid()) }
    }

    @Test
    fun `return false when deleting non-existent product`() = runBlocking {
        val productId = Uuid.random()
        coEvery { productRepository.delete(productId.toJavaUuid()) } returns false

        val result = productService.delete(productId)

        assertTrue(!result)
        coVerify { productRepository.delete(productId.toJavaUuid()) }
    }
}
