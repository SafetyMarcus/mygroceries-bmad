package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import kotlin.uuid.*

class CategoryService(
    private val categoryRepository: CategoryRepository = CategoryRepository,
) {

    fun create(categoryName: NewCategory) = categoryRepository.create(categoryName)

    fun readAll() = categoryRepository.readAll()

    fun readById(id: Uuid): Category? = categoryRepository.readById(id.toString())

    fun update(category: Category): Category? = categoryRepository
        .update(category)
        .takeIf { it }
        ?.let { categoryRepository.readById(category.id.toString()!!) }

    fun delete(id: Uuid): Boolean = categoryRepository.delete(id.toString())
}