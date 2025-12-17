package com.safetymarcus.mygroceries.validators

import com.safetymarcus.mygroceries.model.Product
import com.safetymarcus.mygroceries.model.NewProduct
import com.safetymarcus.mygroceries.model.ProductId
import com.safetymarcus.mygroceries.model.ProductName
import kotlin.uuid.Uuid

fun Product.validate() = id.validate() + name.validate() + categoryId.validate()

fun NewProduct.validate() = name.validate() + categoryId.validate()

private fun ProductId?.validate() = takeIf { it != null }?.let { Validator.Valid } ?: Validator.Invalid("Product ID cannot be null")

private fun ProductName.validate() = takeIf { it.isNotBlank() }?.let { Validator.Valid } ?: Validator.Invalid("Product name cannot be blank")
