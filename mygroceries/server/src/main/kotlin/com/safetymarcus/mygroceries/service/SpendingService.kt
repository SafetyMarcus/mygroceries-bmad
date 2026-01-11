package com.safetymarcus.mygroceries.service

import com.safetymarcus.mygroceries.db.CategorySpendingRepository

class SpendingService(private val repository: CategorySpendingRepository) {
    suspend fun getCategorySpending() = repository.getCategorySpending()
}
