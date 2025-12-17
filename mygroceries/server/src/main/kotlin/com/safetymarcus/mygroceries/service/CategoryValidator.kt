package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import io.ktor.server.plugins.requestvalidation.*
import java.util.UUID

fun Category.validate(): ValidationResult = id.validate() + name.validate()

fun NewCategory.validate(): ValidationResult = name.validate()

typealias CategoryId = UUID

private fun CategoryId?.validate() = takeIf { it != null }?.let { ValidationResult.Valid } ?: ValidationResult.Invalid("Category id cannot be null")

typealias CategoryName = String

private fun CategoryName.validate() = takeIf { it.isNotBlank() }?.let { ValidationResult.Valid } ?: ValidationResult.Invalid("Category name cannot be blank")