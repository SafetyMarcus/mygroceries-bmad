package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlin.uuid.*

class CategoryService(
    private val categoryRepository: CategoryRepository = CategoryRepository,
) {
    suspend fun create(categoryName: NewCategory) = categoryRepository.create(categoryName)

    suspend fun readAll() = categoryRepository.readAll()

    suspend fun readById(id: Uuid) = categoryRepository.readById(id.toString())

    suspend fun update(category: Category) = categoryRepository
        .update(category)
        .takeIf { it }
        ?.let { categoryRepository.readById(category.id.toString()!!) }

    suspend fun delete(id: Uuid) = categoryRepository.delete(id.toString())

    context(call: ApplicationCall)
    suspend fun validateCategoryExists(categoryId: Uuid) {
        if (readById(categoryId) == null) {
            call.respond(HttpStatusCode.BadRequest, "Category does not exist")
            return
        }
    }
}