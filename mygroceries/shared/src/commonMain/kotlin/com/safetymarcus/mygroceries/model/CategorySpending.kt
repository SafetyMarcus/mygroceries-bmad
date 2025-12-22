package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class CategorySpending(
    @Serializable(with = UUIDSerializer::class)
    val categoryId: CategoryId?,
    val categoryName: CategoryName,
    val totalSpend: Double
)
