package io.github.alanrb.opentv

import android.app.Application
import io.github.alanrb.opentv.di.appModule
import io.github.alanrb.opentv.di.dataModule
import io.github.alanrb.opentv.di.databaseModule
import io.github.alanrb.opentv.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(listOf(appModule, dataModule, databaseModule, networkModule))
        }
    }
}