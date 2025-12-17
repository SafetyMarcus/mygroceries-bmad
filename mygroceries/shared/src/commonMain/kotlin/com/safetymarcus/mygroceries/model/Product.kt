package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import kotlin.uuid.*

/**
 * Represents a product in the system.
 * @property id Unique identifier for the product (auto-generated if not provided)
 * @property name The name of the product
 * @property categoryId The ID of the category this product belongs to
 */
@Serializable
data class Product(
    @Serializable(with = UUIDSerializer::class)
    val id: ProductId? = Uuid.random(),
    val name: ProductName,
    @Serializable(with = UUIDSerializer::class)
    val categoryId: CategoryId?
) {
    constructor(stringId: String, name: String, categoryId: String) : this(
        id = Uuid.parse(stringId),
        name = name,
        categoryId = Uuid.parse(categoryId)
    )
}

/**
 * Data transfer object for creating a new product.
 * @property name The name of the new product
 * @property categoryId The ID of the category this product belongs to
 */
@Serializable
data class NewProduct(
    val name: ProductName,
    @Serializable(with = UUIDSerializer::class)
    val categoryId: CategoryId?
)