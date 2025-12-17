package com.safetymarcus.mygroceries.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import kotlin.uuid.*

suspend fun ApplicationCall.getAndValidateUUID(name: String): Uuid {
    return try {
        val param = parameters[name]!!.toString()
        Uuid.parse(param)
    } catch (e: Exception) {
        respond(HttpStatusCode.BadRequest, "Invalid UUID: $name") 
        throw e
    }
}
