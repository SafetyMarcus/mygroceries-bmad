package com.safetymarcus.mygroceries.presentation.ui.components

import CategorySpendingChart
import CategorySpendingState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.safetymarcus.mygroceries.model.CategorySpending
import kotlin.uuid.Uuid

@Composable
fun DashboardScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CategorySpendingChart(
            state = CategorySpendingState.Success(
                data = listOf(
                    CategorySpending(Uuid.random(), "Produce", 30.0),
                    CategorySpending(Uuid.random(), "Dairy", 20.0),
                    CategorySpending(Uuid.random(), "Meat", 15.0),
                    CategorySpending(Uuid.random(), "Bakery", 10.0),
                    CategorySpending(Uuid.random(), "Other", 25.0)
                )
            )
        )
    }
}