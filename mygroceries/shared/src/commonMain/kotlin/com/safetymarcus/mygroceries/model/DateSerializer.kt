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
import kotlin.time.Instant
import kotlin.time.Clock

object DateSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant {
        return try {
            val instantString = decoder.decodeString()
            if (instantString.isBlank()) Clock.System.now() else Instant.parse(instantString)
        } catch (e: IllegalArgumentException) {
            throw SerializationException("Failed to deserialize Instant: ${e.message}")
        }
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }
}