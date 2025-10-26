package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import io.ktor.server.plugins.requestvalidation.*
import java.util.UUID

fun Category.validate(): ValidationResult = id.validate() + name.validate()

fun NewCategory.validate(): ValidationResult = name.validate()

private fun UUID?.validate() = takeIf { it != null }?.let { ValidationResult.Valid } ?: ValidationResult.Invalid("Category id cannot be null")

private fun String.validate() = takeIf { it.isNotBlank() }?.let { ValidationResult.Valid } ?: ValidationResult.Invalid("Category name cannot be blank")

operator fun ValidationResult.plus(other: ValidationResult) = when {
    this is ValidationResult.Valid -> other
    else -> this
}