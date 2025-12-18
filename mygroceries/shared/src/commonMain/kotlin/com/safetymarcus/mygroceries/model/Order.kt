package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import com.safetymarcus.mygroceries.model.OrderId
import java.time.Instant
import java.util.*

@Serializable
data class Order(
    val id: OrderId? = OrderId.random(),
    val orderDate: Instant,
)

data class NewOrder(
    val orderDate: Instant,
)