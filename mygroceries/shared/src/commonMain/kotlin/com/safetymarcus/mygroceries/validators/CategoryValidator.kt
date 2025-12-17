package com.safetymarcus.mygroceries.validators

import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import com.safetymarcus.mygroceries.model.CategoryId
import com.safetymarcus.mygroceries.model.CategoryName
import kotlin.uuid.Uuid

fun Category.validate() = id.validate() + name.validate()

fun NewCategory.validate() = name.validate()

private fun CategoryId?.validate() = takeIf { it != null }?.let { Validator.Valid } ?: Validator.Invalid("Category id cannot be null")

private fun CategoryName.validate() = takeIf { it.isNotBlank() }?.let { Validator.Valid } ?: Validator.Invalid("Category name cannot be blank")
