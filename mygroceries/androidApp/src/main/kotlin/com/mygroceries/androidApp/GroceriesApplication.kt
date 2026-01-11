package com.mygroceries.androidApp

import android.app.Application
import com.safetymarcus.mygroceries.di.initKoin

class GroceriesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
