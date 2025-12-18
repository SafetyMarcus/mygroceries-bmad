package com.safetymarcus.mygroceries.validators

import com.safetymarcus.mygroceries.model.LineItem
import com.safetymarcus.mygroceries.model.NewLineItem
import com.safetymarcus.mygroceries.model.LineItemId
import com.safetymarcus.mygroceries.model.ProductId
import com.safetymarcus.mygroceries.model.OrderId

fun LineItem.validate() = id.validate() + 
    productId.validate() + 
    orderId.validate() + 
    quantity.validate() + 
    cost.validate()

fun NewLineItem.validate() = 
    productId.validate() + 
    quantity.validate() + 
    cost.validate()

private fun LineItemId?.validate() = takeIf { it != null }?.let { Validator.Valid } 
    ?: Validator.Invalid("Line item ID cannot be null")

private fun Double.validate() = takeIf { it > 0 }?.let { Validator.Valid } 
    ?: Validator.Invalid("Quantity must be greater than 0")

private fun Int.validate() = takeIf { it >= 0 }
    ?.let { Validator.Valid }
    ?: Validator.Invalid("Cost cannot be null")
