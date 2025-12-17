package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Category(
    @Serializable(with = UUIDSerializer::class) val id: CategoryId? = UUID.randomUUID(),
    val name: CategoryName
)

@Serializable
data class NewCategory(
    val name: CategoryName
)