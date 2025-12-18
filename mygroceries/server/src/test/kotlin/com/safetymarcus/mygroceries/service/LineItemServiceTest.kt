package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.LineItemRepository
import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewLineItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import kotlin.uuid.*

class LineItemServiceTest {

    private val lineItemRepository = mockk<LineItemRepository>()
    private val lineItemService = LineItemService(lineItemRepository)
    private val testOrderId = Uuid.random()
    private val testProductId = Uuid.random()
    private val testLineItemId = Uuid.random()

    @Test
    fun `createLineItem with valid data should return created line item`() = runBlocking {
        val newLineItem = NewLineItem(
            productId = testProductId,
            quantity = 2.0,
            cost = 399  // $3.99 in cents
        )
        val expectedLineItem = LineItem(
            id = testLineItemId,
            orderId = testOrderId,
            productId = testProductId,
            quantity = 2.0,
            cost = 399  // $3.99 in cents
        )
        
        coEvery { lineItemRepository.create(testOrderId.toJavaUuid(), newLineItem) } returns expectedLineItem
        
        val result = lineItemService.createLineItem(testOrderId, newLineItem)
        
        assertEquals(expectedLineItem, result)
        coVerify { lineItemRepository.create(testOrderId.toJavaUuid(), newLineItem) }
    }

    @Test
    fun `getLineItem with valid id should return line item`() = runBlocking {
        val expectedLineItem = LineItem(
            id = testLineItemId,
            orderId = testOrderId,
            productId = testProductId,
            quantity = 2.0,
            cost = 399  // $3.99 in cents
        )
        
        coEvery { lineItemRepository.readById(testLineItemId.toJavaUuid()) } returns expectedLineItem
        
        val result = lineItemService.getLineItem(testLineItemId)
        
        assertEquals(expectedLineItem, result)
        coVerify { lineItemRepository.readById(testLineItemId.toJavaUuid()) }
    }

    @Test
    fun `getLineItemsByOrder should return all line items for order`() = runBlocking {
        val lineItems = listOf(
            LineItem(
                id = testLineItemId,
                orderId = testOrderId,
                productId = testProductId,
                quantity = 3.0,
                cost = 499  // $4.99 in cents
            ),
            LineItem(
                id = Uuid.random(),
                orderId = testOrderId,
                productId = Uuid.random(),
                quantity = 1.0,
                cost = 249  // $2.49 in cents
            )
        )
        
        coEvery { lineItemRepository.findByOrderId(testOrderId.toJavaUuid()) } returns lineItems
        
        val result = lineItemService.getLineItemsByOrder(testOrderId)
        
        assertEquals(lineItems, result)
        coVerify { lineItemRepository.findByOrderId(testOrderId.toJavaUuid()) }
    }

    @Test
    fun `updateLineItem with valid data should return updated line item`() = runBlocking {
        val updatedLineItem = LineItem(
            id = testLineItemId,
            orderId = testOrderId,
            productId = testProductId,
            quantity = 3.0,
            cost = 499
        )
        
        coEvery { lineItemRepository.update(updatedLineItem) } returns true
        coEvery { lineItemRepository.readById(testLineItemId.toJavaUuid()) } returns updatedLineItem
        
        val result = lineItemService.updateLineItem(updatedLineItem)
        
        assertEquals(updatedLineItem, result)
        coVerify { 
            lineItemRepository.update(updatedLineItem)
            lineItemRepository.readById(testLineItemId.toJavaUuid())
        }
    }

    @Test
    fun `updateLineItem with non-existent line item should return null`() = runBlocking {
        val lineItem = LineItem(
            id = testLineItemId,
            orderId = testOrderId,
            productId = testProductId,
            quantity = 1.0,
            cost = 299
        )
        
        coEvery { lineItemRepository.update(lineItem) } returns false
        
        val result = lineItemService.updateLineItem(lineItem)
        
        assertNull(result)
        coVerify { lineItemRepository.update(lineItem) }
        coVerify(exactly = 0) { lineItemRepository.readById(any()) }
    }

    @Test
    fun `deleteLineItem with valid id should return true`() = runBlocking {
        coEvery { lineItemRepository.delete(testLineItemId.toJavaUuid()) } returns true
        
        val result = lineItemService.deleteLineItem(testLineItemId)
        
        assertEquals(true, result)
        coVerify { lineItemRepository.delete(testLineItemId.toJavaUuid()) }
    }
}
