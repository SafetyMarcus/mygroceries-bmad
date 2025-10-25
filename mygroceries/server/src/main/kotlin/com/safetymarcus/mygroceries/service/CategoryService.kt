package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import java.util.UUID

class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val categoryValidator: CategoryValidator
) {

    fun create(category: Category): Category {
        categoryValidator.validateCategory(category)
        val newCategory = category.copy(id = UUID.randomUUID())
        return categoryRepository.create(newCategory)
    }

    fun readAll(): List<Category> {
        return categoryRepository.readAll()
    }

    fun readById(id: UUID): Category? {
        return categoryRepository.readById(id)
    }

    fun update(id: UUID, category: Category): Category? {
        categoryValidator.validateCategory(category)
        val updatedRows = categoryRepository.update(id, category)
        return if (updatedRows > 0) {
            categoryRepository.readById(id)
        } else {
            null
        }
    }

    fun delete(id: UUID): Boolean {
        return categoryRepository.delete(id) > 0
    }
}
