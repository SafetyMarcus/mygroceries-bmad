package com.safetymarcus.mygroceries.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

@Serializable
data class Category(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = UUID.randomUUID(),
    val name: String
)

@Serializable
data class NewCategory(
    val name: String
)

object UUIDSerializer : KSerializer<UUID?> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = try { UUID.fromString(decoder.decodeString()) } catch (e: Exception) { null }

    override fun serialize(encoder: Encoder, value: UUID?) = encoder.encodeString(value.toString())
}