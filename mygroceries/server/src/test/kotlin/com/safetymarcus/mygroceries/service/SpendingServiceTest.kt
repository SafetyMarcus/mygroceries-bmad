package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategorySpendingRepository
import com.safetymarcus.mygroceries.model.CategorySpending
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid

class SpendingServiceTest {

    private val spendingRepository = mockk<CategorySpendingRepository>()
    private val spendingService = SpendingService(spendingRepository)

    @Test
    fun `get category spending returns data from repository`() = runBlocking {
        val spendingList = listOf(
            CategorySpending(Uuid.random(), "Fruits", 10.50),
            CategorySpending(Uuid.random(), "Vegetables", 5.00)
        )
        coEvery { spendingRepository.getCategorySpending() } returns spendingList

        val result = spendingService.getCategorySpending()
        assertEquals(spendingList, result)
    }
}
