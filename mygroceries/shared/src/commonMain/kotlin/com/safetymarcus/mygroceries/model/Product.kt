package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Product(
    @Serializable(with = UUIDSerializer::class)
    val id: ProductId? = UUID.randomUUID(),
    val name: ProductName,
    @Serializable(with = UUIDSerializer::class)
    val categoryId: CategoryId?
)

@Serializable
data class NewProduct(
    val name: ProductName,
    @Serializable(with = UUIDSerializer::class)
    val categoryId: CategoryId?
)