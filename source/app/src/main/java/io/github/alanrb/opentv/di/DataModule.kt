package io.github.alanrb.opentv.di

import io.github.alanrb.opentv.data.SharePreferenceManager
import io.github.alanrb.opentv.data.repo.MovieRepositoryImpl
import io.github.alanrb.opentv.domain.repo.MovieRepository
import org.koin.dsl.module

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

val dataModule = module {
    single<SharePreferenceManager> { SharePreferenceManager.SharePreferenceManagerImpl(get()) }

    single<MovieRepository> { MovieRepositoryImpl(get(), get(), get()) }
}