package com.safetymarcus.mygroceries.db

import com.safetymarcus.mygroceries.model.CategorySpending
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.core.*
import kotlin.uuid.toKotlinUuid

object CategorySpendingRepository {
    private fun toCategorySpending(row: ResultRow) = CategorySpending(
        categoryId = row[CategorySpendingView.categoryId].toKotlinUuid(),
        categoryName = row[CategorySpendingView.categoryName],
        totalSpend = row[CategorySpendingView.totalSpentCents].toDouble() / 100.0
    )

    suspend fun getCategorySpending() = dbQuery {
        CategorySpendingView.selectAll().map { toCategorySpending(it) }
    }
}
