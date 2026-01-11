package com.safetymarcus.mygroceries.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safetymarcus.mygroceries.model.*
import com.safetymarcus.mygroceries.repository.GroceriesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

data class GroceriesUiState(
    val categories: UiState<List<Category>> = UiState.Loading,
    val products: UiState<List<Product>> = UiState.Loading,
    val orders: UiState<List<Order>> = UiState.Loading,
    val spending: UiState<List<CategorySpending>> = UiState.Loading
)

class GroceriesViewModel(private val repository: GroceriesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(GroceriesUiState())
    val uiState: StateFlow<GroceriesUiState> = _uiState.asStateFlow()

    init {
        refreshAll()
    }

    fun refreshAll() {
        fetchCategories()
        fetchProducts()
        fetchOrders()
        fetchSpending()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(categories = UiState.Loading) }
            repository.getCategories().collect { result ->
                _uiState.update { state ->
                    state.copy(
                        categories = result.toUiState()
                    )
                }
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(products = UiState.Loading) }
            repository.getProducts().collect { result ->
                _uiState.update { state ->
                    state.copy(
                        products = result.toUiState()
                    )
                }
            }
        }
    }

    fun fetchOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(orders = UiState.Loading) }
            repository.getOrders().collect { result ->
                _uiState.update { state ->
                    state.copy(
                        orders = result.toUiState()
                    )
                }
            }
        }
    }

    fun fetchSpending() {
        viewModelScope.launch {
            _uiState.update { it.copy(spending = UiState.Loading) }
            repository.getCategorySpending().collect { result ->
                _uiState.update { state ->
                    state.copy(
                        spending = result.toUiState()
                    )
                }
            }
        }
    }

    private fun <T> Result<T>.toUiState(): UiState<T> = fold(
        onSuccess = { UiState.Success(it) },
        onFailure = { UiState.Error(it.message ?: "Unknown error") }
    )
}
