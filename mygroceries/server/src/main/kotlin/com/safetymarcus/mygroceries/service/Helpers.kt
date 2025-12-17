package com.safetymarcus.mygroceries.service

import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import java.util.UUID

operator fun ValidationResult.plus(other: ValidationResult) = when {
    this is ValidationResult.Valid -> other
    else -> this
}

suspend fun ApplicationCall.getAndValidateUUID(name: String): UUID {
    val param = parameters[name]?.toString()
    return try {
        UUID.fromString(param) 
    } catch (e: Exception) {
        respond(HttpStatusCode.BadRequest, "Invalid UUID: $param") 
        throw e
    }
}
