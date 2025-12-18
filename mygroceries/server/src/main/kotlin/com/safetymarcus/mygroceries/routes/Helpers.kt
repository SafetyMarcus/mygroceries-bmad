package com.safetymarcus.mygroceries.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlin.uuid.*
import com.safetymarcus.mygroceries.service.CategoryService
import com.safetymarcus.mygroceries.service.ProductService
import com.safetymarcus.mygroceries.service.OrderService

suspend fun ApplicationCall.getAndValidateUUID(name: String): Uuid {
    return try {
        val param = parameters[name]!!.toString()
        Uuid.parse(param)
    } catch (e: Exception) {
        respond(HttpStatusCode.BadRequest, "Invalid UUID: $name") 
        throw e
    }
}