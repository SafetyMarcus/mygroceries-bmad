package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import kotlin.uuid.*

/**
 * Represents a product category in the system.
 * @property id Unique identifier for the category (auto-generated if not provided)
 * @property name The display name of the category
 */
@Serializable
data class Category(
    @Serializable(with = UUIDSerializer::class)
    val id: CategoryId? = Uuid.random(),
    val name: CategoryName
) {
    constructor(stringId: String, name: String) : this(
        id = Uuid.parse(stringId),
        name = name
    )
}

/**
 * Data transfer object for creating a new category.
 * @property name The display name of the new category
 */
@Serializable
data class NewCategory(
    val name: CategoryName
)