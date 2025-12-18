package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.OrderRepository
import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.model.NewOrder 
import kotlin.uuid.Uuid

class OrderService(
    private val orderRepository: OrderRepository,
) {
    suspend fun createOrder(order: NewOrder) = orderRepository.create(order)
    
    suspend fun getOrder(id: Uuid) = orderRepository.readById(id.toString())
    
    suspend fun getAllOrders() = orderRepository.readAll()
    
    suspend fun updateOrder(order: Order) = orderRepository
        .update(order)
        .takeIf { it }
        ?.let { orderRepository.readById(order.id.toString()) }
    
    suspend fun deleteOrder(id: Uuid) = orderRepository.delete(id.toString())

    context(call: ApplicationCall)
    suspend fun validateOrderExists(orderId: Uuid) {
        if (getOrder(orderId) == null) {
            call.respond(HttpStatusCode.BadRequest, "Order does not exist")
            return
        }
    }
}
