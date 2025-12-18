package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import com.safetymarcus.mygroceries.model.OrderId
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class Order(
    @Serializable(with = UUIDSerializer::class)
    val id: OrderId? = Uuid.random(),
    val date: Instant,
) {
    constructor(stringId: String, date: Instant): this(Uuid.parse(stringId), date)
}

data class NewOrder(
    val date: Instant,
)