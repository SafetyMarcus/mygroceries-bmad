package com.safetymarcus.mygroceries.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import java.util.UUID

suspend fun ApplicationCall.getAndValidateUUID(name: String): UUID {
    val param = parameters[name]?.toString()
    return try {
        UUID.fromString(param) 
    } catch (e: Exception) {
        respond(HttpStatusCode.BadRequest, "Invalid UUID: $param") 
        throw e
    }
}
