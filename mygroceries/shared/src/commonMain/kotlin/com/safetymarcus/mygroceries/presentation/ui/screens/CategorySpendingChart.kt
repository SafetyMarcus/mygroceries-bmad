package com.safetymarcus.mygroceries.presentation.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi

// Sample data for the pie chart
data class CategoryData(val category: String, val value: Double)

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun CategorySpendingChart() {
    val categories = remember { listOf("Produce", "Dairy", "Meat", "Bakery", "Other") }
    val values = remember { listOf(30f, 20f, 15f, 10f, 25f) }

    PieChart(
        values = values,
        label = { Text(categories[it]) }
    )

}
