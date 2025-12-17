package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Product
import com.safetymarcus.mygroceries.model.NewProduct
import io.ktor.server.plugins.requestvalidation.*
import java.util.UUID

fun Product.validate(): ValidationResult = id.validate() + name.validate() + categoryId.validate()

fun NewProduct.validate(): ValidationResult = name.validate() + categoryId.validate()

typealias ProductId = UUID

private fun ProductId?.validate() = takeIf { it != null }?.let { ValidationResult.Valid } ?: ValidationResult.Invalid("Product id cannot be null")

typealias ProductName = String

private fun ProductName.validate() = takeIf { it.isNotBlank() }?.let { ValidationResult.Valid } ?: ValidationResult.Invalid("Product name cannot be blank")
