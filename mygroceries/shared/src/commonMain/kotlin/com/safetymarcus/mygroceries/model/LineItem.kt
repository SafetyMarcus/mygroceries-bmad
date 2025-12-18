package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import com.safetymarcus.mygroceries.model.LineItemId
import com.safetymarcus.mygroceries.model.OrderId
import com.safetymarcus.mygroceries.model.ProductId
import kotlin.uuid.*

@Serializable
data class LineItem(
    @Serializable(with = UUIDSerializer::class)
    val id: LineItemId? = Uuid.random(),
    @Serializable(with = UUIDSerializer::class)
    val orderId: OrderId?,
    @Serializable(with = UUIDSerializer::class)
    val productId: ProductId?,
    val quantity: Double,
    val cost: Double,
    val product: Product? = null
)

@Serializable
data class NewLineItem(
    @Serializable(with = UUIDSerializer::class)
    val productId: ProductId?,
    val quantity: Double,
    val cost: Double,
    val product: Product? = null
)
