package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CategoryServiceTest {

    private val categoryRepository = mockk<CategoryRepository>()
    private val categoryValidator = CategoryValidator() // Use real validator for unit tests
    private val categoryService = CategoryService(categoryRepository, categoryValidator)

    @Test
    fun `create category with valid data`() {
        val category = Category(id = UUID.randomUUID(), name = "Fruits")
        val newId = UUID.randomUUID()
        val newCategory = category.copy(id = newId)

        coEvery { categoryRepository.create(any()) } returns newCategory

        val result = categoryService.create(category)

        assertEquals("Fruits", result.name)
        assertEquals(newId, result.id)
    }

    @Test
    fun `create category with blank name should throw exception`() {
        val category = Category(id = UUID.randomUUID(), name = " ")

        val exception = assertFailsWith<IllegalArgumentException> {
            categoryService.create(category)
        }
        assertEquals("Category name cannot be empty.", exception.message)
    }

    @Test
    fun `get category by valid id`() {
        val id = UUID.randomUUID()
        val category = Category(id = id, name = "Fruits")

        coEvery { categoryRepository.readById(id) } returns category

        val result = categoryService.readById(id)

        assertEquals(category, result)
    }

    @Test
    fun `update category with valid data`() {
        val id = UUID.randomUUID()
        val category = Category(id = id, name = "Vegetables")

        coEvery { categoryRepository.update(id, category) } returns 1
        coEvery { categoryRepository.readById(id) } returns category

        val result = categoryService.update(id, category)

        assertEquals(category, result)
    }

    @Test
    fun `update category with blank name should throw exception`() {
        val id = UUID.randomUUID()
        val category = Category(id = id, name = " ")

        val exception = assertFailsWith<IllegalArgumentException> {
            categoryService.update(id, category)
        }
        assertEquals("Category name cannot be empty.", exception.message)
    }

    @Test
    fun `delete category with valid id`() {
        val id = UUID.randomUUID()

        coEvery { categoryRepository.delete(id) } returns 1

        val result = categoryService.delete(id)

        assertEquals(true, result)
    }
}