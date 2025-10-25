package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.model.Category
import java.util.UUID

class CategoryValidator {
    fun validateCategory(category: Category) {
        if (category.name.isBlank()) {
            throw IllegalArgumentException("Category name cannot be empty.")
        }
    }
}
