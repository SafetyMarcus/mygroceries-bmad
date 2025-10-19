package com.safetymarcus.mygroceries.androidApp

import android.app.Application
import com.safetymarcus.mygroceries.di.initKoin

class MuseumApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
