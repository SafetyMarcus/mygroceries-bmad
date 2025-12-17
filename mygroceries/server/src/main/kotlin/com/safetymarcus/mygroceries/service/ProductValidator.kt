package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Product
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.ProductId
import io.ktor.server.plugins.requestvalidation.*
import java.util.UUID

fun Product.validate(): ValidationResult = id.validate() + categoryId.validate()

fun NewProduct.validate(): ValidationResult = categoryId.validate()

private fun ProductId?.validate() = takeIf { it != null }?.let { ValidationResult.Valid } ?: ValidationResult.Invalid("ID cannot be null")
