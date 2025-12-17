package com.safetymarcus.mygroceries.service

import io.ktor.server.plugins.requestvalidation.*

operator fun ValidationResult.plus(other: ValidationResult) = when {
    this is ValidationResult.Valid -> other
    else -> this
}