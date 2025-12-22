package com.safetymarcus.mygroceries.presentation.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi

// Sample data for the pie chart
data class CategoryData(val category: String, val value: Double)

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun CategorySpendingChart() {
    val categories = remember { listOf("Produce", "Dairy", "Meat", "Bakery", "Other") }
    val values = remember { listOf(30f, 20f, 15f, 10f, 25f) }

    val data = mapOf(
        "category" to categories,
        "value" to values
    )

    PieChart(
        values = values,
        label = { Text(categories[it]) }
    )

}
