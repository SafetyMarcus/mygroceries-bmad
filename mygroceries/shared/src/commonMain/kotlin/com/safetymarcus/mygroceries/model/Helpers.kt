package com.safetymarcus.mygroceries.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.SerializationException
import kotlin.uuid.*
import kotlin.uuid.ExperimentalUuidApi

object UUIDSerializer : KSerializer<Uuid?> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Uuid? {
        return try {
            val uuidString = decoder.decodeString()
            if (uuidString.isBlank()) null else Uuid.parse(uuidString)
        } catch (e: IllegalArgumentException) {
            throw SerializationException("Failed to deserialize UUID: ${e.message}")
        }
    }

    override fun serialize(encoder: Encoder, value: Uuid?) {
        encoder.encodeString(value?.toString() ?: "")
    }
}