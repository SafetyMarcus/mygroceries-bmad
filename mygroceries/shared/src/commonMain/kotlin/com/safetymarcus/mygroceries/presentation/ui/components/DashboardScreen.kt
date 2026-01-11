package com.safetymarcus.mygroceries.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.safetymarcus.mygroceries.presentation.GroceriesViewModel
import com.safetymarcus.mygroceries.presentation.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(viewModel: GroceriesViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val spendingState = when (val spending = uiState.spending) {
            is UiState.Loading -> CategorySpendingState.Loading
            is UiState.Error -> CategorySpendingState.Error(spending.message)
            is UiState.Success -> CategorySpendingState.Success(spending.data)
        }

        CategorySpendingChart(state = spendingState)
    }
}