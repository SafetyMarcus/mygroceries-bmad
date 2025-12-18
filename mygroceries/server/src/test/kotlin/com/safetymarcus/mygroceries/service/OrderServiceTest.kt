package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.OrderRepository
import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.model.NewOrder
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.uuid.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class OrderServiceTest {

    private val orderRepository = mockk<OrderRepository>()
    private val orderService = OrderService(orderRepository)

    @Test
    fun `createOrder with valid data should return created order`() = runBlocking {
        val testDate = Clock.System.now()
        val newOrder = NewOrder(date = testDate)
        val expectedOrder = Order(id = Uuid.random(), date = testDate)
        
        coEvery { orderRepository.create(newOrder) } returns expectedOrder
        
        val result = orderService.createOrder(newOrder)
        
        assertEquals(expectedOrder, result)
        coVerify { orderRepository.create(newOrder) }
    }

    @Test
    fun `getOrder with valid id should return order`() = runBlocking {
        val orderId = Uuid.random()
        val testDate = Clock.System.now()
        val expectedOrder = Order(id = orderId, date = testDate)
        
        coEvery { orderRepository.readById(orderId.toJavaUuid()) } returns expectedOrder
        
        val result = orderService.getOrder(orderId)
        
        assertEquals(expectedOrder, result)
        coVerify { orderRepository.readById(orderId.toJavaUuid()) }
    }

    @Test
    fun `getAllOrders should return all orders`() = runBlocking {
        val testDate = Clock.System.now()
        val orders = listOf(
            Order(id = Uuid.random(), date = testDate),
            Order(id = Uuid.random(), date = testDate - 1.days)
        )
        
        coEvery { orderRepository.readAll() } returns orders
        
        val result = orderService.getAllOrders()
        
        assertEquals(orders, result)
        coVerify { orderRepository.readAll() }
    }

    @Test
    fun `updateOrder with valid data should return updated order`() = runBlocking {
        val orderId = Uuid.random()
        val testDate = Clock.System.now()
        val updatedOrder = Order(id = orderId, date = testDate)
        
        coEvery { orderRepository.update(updatedOrder) } returns true
        coEvery { orderRepository.readById(orderId.toJavaUuid()) } returns updatedOrder
        
        val result = orderService.updateOrder(updatedOrder)
        
        assertEquals(updatedOrder, result)
        coVerify { 
            orderRepository.update(updatedOrder)
            orderRepository.readById(orderId.toJavaUuid())
        }
    }

    @Test
    fun `updateOrder with non-existent order should return null`() = runBlocking {
        val orderId = Uuid.random()
        val order = Order(id = orderId, date = Clock.System.now())
        
        coEvery { orderRepository.update(order) } returns false
        
        val result = orderService.updateOrder(order)
        
        assertEquals(null, result)
        coVerify { orderRepository.update(order) }
        coVerify(exactly = 0) { orderRepository.readById(any()) }
    }

    @Test
    fun `deleteOrder with valid id should return true`() = runBlocking {
        val orderId = Uuid.random()
        
        coEvery { orderRepository.delete(orderId.toJavaUuid()) } returns true
        
        val result = orderService.deleteOrder(orderId)
        
        assertEquals(true, result)
        coVerify { orderRepository.delete(orderId.toJavaUuid()) }
    }
}
