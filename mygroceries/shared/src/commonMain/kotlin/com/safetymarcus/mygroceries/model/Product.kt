package com.safetymarcus.mygroceries.model

import kotlinx.serialization.Serializable
import java.util.UUID
import com.safetymarcus.mygroceries.service.CategoryId
import com.safetymarcus.mygroceries.service.ProductId
import com.safetymarcus.mygroceries.service.ProductName

@Serializable
data class Product(
    val id: ProductId = UUID.randomUUID(),
    val name: ProductName,
    val categoryId: CategoryId
)

@Serializable
data class NewProduct(
    val name: ProductName,
    val categoryId: CategoryId
)