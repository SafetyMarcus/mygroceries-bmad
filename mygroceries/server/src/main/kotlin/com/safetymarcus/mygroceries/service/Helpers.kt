package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.validators.Validator
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun Validator.result() = when {
    this is Validator.Invalid -> ValidationResult.Invalid(this.message)
    else -> ValidationResult.Valid
}