package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import java.util.UUID

class CategoryService(private val categoryRepository: CategoryRepository) {

    fun create(category: Category): Category {
        validateCategory(category)
        val newCategory = category.copy(id = UUID.randomUUID().toString())
        return categoryRepository.create(newCategory)
    }

    fun readAll(): List<Category> {
        return categoryRepository.readAll()
    }

    fun readById(id: String): Category? {
        validateUuid(id)
        return categoryRepository.readById(id)
    }

    fun update(id: String, category: Category): Category? {
        validateUuid(id)
        validateCategory(category)
        val updatedRows = categoryRepository.update(id, category)
        return if (updatedRows > 0) {
            categoryRepository.readById(id)
        } else {
            null
        }
    }

    fun delete(id: String): Boolean {
        validateUuid(id)
        return categoryRepository.delete(id) > 0
    }

    private fun validateCategory(category: Category) {
        if (category.name.isBlank()) {
            throw IllegalArgumentException("Category name cannot be empty.")
        }
    }

    private fun validateUuid(id: String) {
        try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid UUID format for id.")
        }
    }
}
