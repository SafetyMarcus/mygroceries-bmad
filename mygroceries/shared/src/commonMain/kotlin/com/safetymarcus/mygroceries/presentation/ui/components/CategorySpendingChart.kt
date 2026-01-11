package com.safetymarcus.mygroceries.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.safetymarcus.mygroceries.model.CategoryName
import com.safetymarcus.mygroceries.model.CategorySpending
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlin.math.roundToInt

sealed class CategorySpendingState {
    object Loading : CategorySpendingState()
    data class Success(val data: List<CategorySpending>) : CategorySpendingState()
    data class Error(val message: String) : CategorySpendingState()
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun CategorySpendingChart(state: CategorySpendingState) = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    when (state) {
        is CategorySpendingState.Loading -> CircularProgressIndicator()
        is CategorySpendingState.Error -> Error(state)
        is CategorySpendingState.Success -> CategoriesChart(state)
    }
}

@Composable
private fun CategoriesChart(state: CategorySpendingState.Success) {
    val values = remember { state.data.map { it.totalSpend.toFloat() } }
    val categories = remember { state.data.map { it.categoryName } }
    val total = remember(values) { values.sum() }
    val colors = remember(categories.size) {
        List(categories.size) { i ->
            Color.hsv(
                hue = (i.toFloat() / categories.size) * 360f,
                saturation = 0.7f,
                value = 0.9f
            )
        }
    }

    Row {
        Chart(values, total, colors)
        Spacer(Modifier.size(16.dp))
        Legend(categories, colors)
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun Chart(
    values: List<Float>,
    total: Float,
    colors: List<Color>
) = PieChart(
    values = values,
    slice = { DefaultSlice(color = colors[it]) },
    label = { i ->
        val percentage = (values[i] / total) * 100
        val formattedPercentage = ((percentage * 10).roundToInt() / 10f).toString()
        val totalAmount = toDollarsCents(values[i])
        Text(
            text = "$$totalAmount ($formattedPercentage%)",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
)

@Composable
private fun Legend(
    categories: List<CategoryName>,
    colors: List<Color>
) = Column {
    categories.forEachIndexed { i, category ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(colors[i], shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun toDollarsCents(value: Float): String {
    val cents = (value * 100).roundToInt()
    val dollars = cents / 100
    val centsPart = cents % 100
    val valueString = "$dollars.${centsPart.toString().padStart(2, '0')}"
    return valueString
}

@Composable
private fun Error(state: CategorySpendingState.Error) {
    Text(
        text = state.message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
    )
}
