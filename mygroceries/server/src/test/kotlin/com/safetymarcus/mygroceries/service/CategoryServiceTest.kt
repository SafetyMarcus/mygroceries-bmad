package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategoryRepository
import com.safetymarcus.mygroceries.model.Category
import com.safetymarcus.mygroceries.model.NewCategory
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CategoryServiceTest {

    private val categoryRepository = mockk<CategoryRepository>()
    private val categoryService = CategoryService(categoryRepository)

    @Test
    fun `create category with valid data`() {
        val category = NewCategory(name = "Fruits")
        coEvery { categoryRepository.create(any()) } returns Category(name = "Fruits")
        val result = categoryService.create(category)
        assertEquals("Fruits", result.name)
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
        coEvery { categoryRepository.update(category) } returns 1
        coEvery { categoryRepository.readById(id) } returns category
        val result = categoryService.update(category)
        assertEquals(category, result)
    }

    @Test
    fun `delete category with valid id`() {
        val id = UUID.randomUUID()
        coEvery { categoryRepository.delete(id) } returns 1
        val result = categoryService.delete(id)
        assertEquals(true, result)
    }
}