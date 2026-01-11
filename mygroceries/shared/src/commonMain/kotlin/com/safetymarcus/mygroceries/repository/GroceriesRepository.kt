package com.safetymarcus.mygroceries.repository

import com.safetymarcus.mygroceries.model.*
import com.safetymarcus.mygroceries.network.GroceriesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.uuid.Uuid

interface GroceriesRepository {
    fun getCategories(): Flow<Result<List<Category>>>
    suspend fun createCategory(name: CategoryName): Result<Category>
    
    fun getProducts(): Flow<Result<List<Product>>>
    suspend fun createProduct(name: ProductName, categoryId: CategoryId): Result<Product>
    
    fun getOrders(): Flow<Result<List<Order>>>
    suspend fun createOrder(order: NewOrder): Result<Order>

    fun getLineItems(orderId: Uuid): Flow<Result<List<LineItem>>>
    
    fun getCategorySpending(): Flow<Result<List<CategorySpending>>>
}

class GroceriesRepositoryImpl(private val api: GroceriesApi) : GroceriesRepository {
    
    override fun getCategories(): Flow<Result<List<Category>>> = flow {
        try {
            emit(Result.success(api.getCategories()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun createCategory(name: CategoryName): Result<Category> {
        return try {
            Result.success(api.createCategory(NewCategory(name)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getProducts(): Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.success(api.getProducts()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun createProduct(name: ProductName, categoryId: CategoryId): Result<Product> {
        return try {
            Result.success(api.createProduct(NewProduct(name, categoryId)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOrders(): Flow<Result<List<Order>>> = flow {
        try {
            emit(Result.success(api.getOrders()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun createOrder(order: NewOrder): Result<Order> {
        return try {
            Result.success(api.createOrder(order))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLineItems(orderId: Uuid): Flow<Result<List<LineItem>>> = flow {
        try {
            emit(Result.success(api.getLineItems(orderId)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getCategorySpending(): Flow<Result<List<CategorySpending>>> = flow {
        try {
            emit(Result.success(api.getCategorySpending()))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
