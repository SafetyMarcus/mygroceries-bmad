package com.safetymarcus.mygroceries.di

import org.koin.core.context.startKoin
import org.koin.dsl.module

val dataModule = module {
    // No data-related dependencies for now
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
        )
    }
}
