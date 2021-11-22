package io.github.alanrb.opentv.di

import androidx.room.Room
import io.github.alanrb.opentv.data.db.AppDatabase
import org.koin.dsl.module

/**
 * Created by Tuong (Alan) on 6/16/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

val databaseModule = module {

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "OpenTVDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().movieDao() }
}