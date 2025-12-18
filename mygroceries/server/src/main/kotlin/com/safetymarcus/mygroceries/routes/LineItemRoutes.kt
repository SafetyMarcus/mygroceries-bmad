package com.safetymarcus.mygroceries.routes

import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewLineItem
import com.safetymarcus.mygroceries.service.LineItemService
import com.safetymarcus.mygroceries.service.OrderService
import com.safetymarcus.mygroceries.service.ProductService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.uuid.*

fun Route.lineItemRoutes(
    lineItemService: LineItemService,
    productService: ProductService,
    orderService: OrderService
) {
    route("/lineitems") {
        get("/{id}") {
            val id = call.getAndValidateUUID("id")
            val lineItem = lineItemService.getLineItem(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Line item not found"
            )
            call.respond(lineItem)
        }

        put("/{id}") {
            val id = call.getAndValidateUUID("id")
            val updateLineItem = call.receive<LineItem>()

            
            context(call) {
                productService.validateProductExists(updateLineItem.productId!!)
                orderService.validateOrderExists(updateLineItem.orderId!!)
            }

            if (updateLineItem.id != id) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Line item ID in path does not match ID in request body"
                )
                return@put
            }

            lineItemService.updateLineItem(updateLineItem)
                ?.let { call.respond(it) } 
                ?: call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}") {
            val id = call.getAndValidateUUID("id")
            if (lineItemService.deleteLineItem(id)) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound)
        }
    }

    // Nested order routes
    route("/orders/{orderId}/lineitems") {
        post {
            val orderId = call.getAndValidateUUID("orderId")
            val newLineItem = call.receive<NewLineItem>()
            context(call) {
                productService.validateProductExists(newLineItem.productId!!)
                orderService.validateOrderExists(orderId)
            }
            val createdLineItem = lineItemService.createLineItem(
                orderId,
                newLineItem
            )
            call.respond(HttpStatusCode.Created, createdLineItem)
        }

        get {
            val orderId = call.getAndValidateUUID("orderId")
            val lineItems = lineItemService.getLineItemsByOrder(orderId)
            call.respond(lineItems)
        }
    }
}
