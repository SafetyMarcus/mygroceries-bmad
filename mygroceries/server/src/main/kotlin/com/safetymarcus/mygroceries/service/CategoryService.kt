package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import java.util.UUID

class CategoryService(
    private val categoryRepository: CategoryRepository = CategoryRepository,
) {

    fun create(categoryName: NewCategory) = categoryRepository.create(categoryName)

    fun readAll() = categoryRepository.readAll()

    fun readById(id: UUID) = categoryRepository.readById(id)

    fun update(category: Category) = categoryRepository
        .update(category)
        .takeIf { it > 0 }
        ?.let { categoryRepository.readById(category.id!!) }

    fun delete(id: UUID) = categoryRepository.delete(id) > 0
}
