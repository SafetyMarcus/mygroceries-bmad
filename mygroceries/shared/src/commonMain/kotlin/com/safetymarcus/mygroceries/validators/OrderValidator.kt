package com.safetymarcus.mygroceries.validators

import com.safetymarcus.mygroceries.model.Order
import com.safetymarcus.mygroceries.model.NewOrder
import com.safetymarcus.mygroceries.model.OrderId
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock

fun Order.validate() = id.validate() + orderDate.validate()

fun NewOrder.validate() = orderDate.validate()

private fun OrderId?.validate() = takeIf { it != null }?.let { Validator.Valid } 
    ?: Validator.Invalid("Order ID cannot be null")

private fun Instant.validate() = takeIf { it <= Clock.System.now() }?.let { Validator.Valid } 
    ?: Validator.Invalid("Order date cannot be in the future")
