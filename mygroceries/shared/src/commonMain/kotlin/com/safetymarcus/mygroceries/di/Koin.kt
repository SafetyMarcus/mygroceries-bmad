package com.safetymarcus.mygroceries.di

import com.safetymarcus.mygroceries.network.GroceriesApi
import com.safetymarcus.mygroceries.presentation.GroceriesViewModel
import com.safetymarcus.mygroceries.repository.GroceriesRepository
import com.safetymarcus.mygroceries.repository.GroceriesRepositoryImpl
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single { GroceriesApi.createClient(baseUrl = "http://localhost:8081/") } // Default local URL
    single { GroceriesApi(get()) }
    single<GroceriesRepository> { GroceriesRepositoryImpl(get()) }
    viewModelOf(::GroceriesViewModel)
}

fun initKoin() = startKoin { modules(dataModule) }

// For iOS
fun doInitKoin() = initKoin()
