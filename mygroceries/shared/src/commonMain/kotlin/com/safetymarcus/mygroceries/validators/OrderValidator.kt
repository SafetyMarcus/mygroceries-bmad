package com.safetymarcus.mygroceries.validators

import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.model.NewOrder
import com.safetymarcus.mygroceries.model.OrderId
import kotlin.time.Clock
import kotlin.time.Instant

fun Order.validate() = id.validate() + date.validate()

fun NewOrder.validate() = date.validate()

private fun OrderId?.validate() = takeIf { it != null }?.let { Validator.Valid } 
    ?: Validator.Invalid("Order ID cannot be null")

private fun Instant.validate() = takeIf { it <= Clock.System.now() }?.let { Validator.Valid } 
    ?: Validator.Invalid("Order date cannot be in the future")