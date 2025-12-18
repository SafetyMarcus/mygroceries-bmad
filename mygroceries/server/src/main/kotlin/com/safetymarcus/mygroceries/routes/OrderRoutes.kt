package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.model.NewOrder
import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.service.OrderService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.uuid.*

fun Route.orderRoutes(orderService: OrderService) {
    route("/orders") {
        post {
            val newOrder = call.receive<NewOrder>()
            val createdOrder = orderService.createOrder(newOrder)
            call.respond(HttpStatusCode.Created, createdOrder!!)
        }

        get {
            call.respond(orderService.getAllOrders())
        }

        get("/{id}") {
            val id = call.getAndValidateUUID("id")
            val order = orderService.getOrder(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Order not found"
            )
            call.respond(order)
        }

        put("/{id}") {
            val id = call.getAndValidateUUID("id")
            val updateOrder = call.receive<Order>()

            if (updateOrder.id != id) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Order ID in path does not match ID in request body"
                )
                return@put
            }

            orderService.updateOrder(updateOrder)?.let { call.respond(it) } ?: call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}") {
            val id = call.getAndValidateUUID("id")
            if (orderService.deleteOrder(id)) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound, "Order not found")
        }
    }
}
