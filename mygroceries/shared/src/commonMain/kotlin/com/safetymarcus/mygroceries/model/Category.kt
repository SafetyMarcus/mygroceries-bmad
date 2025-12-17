package com.safetymarcus.mygroceries.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID
import com.safetymarcus.mygroceries.service.CategoryName
import com.safetymarcus.mygroceries.service.CategoryId

@Serializable
data class Category(
    @Serializable(with = UUIDSerializer::class) val id: CategoryId? = UUID.randomUUID(),
    val name: CategoryName
)

@Serializable
data class NewCategory(
    val name: CategoryName
)

object UUIDSerializer : KSerializer<UUID?> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = try { UUID.fromString(decoder.decodeString()) } catch (e: Exception) { null }

    override fun serialize(encoder: Encoder, value: UUID?) = encoder.encodeString(value.toString())
}