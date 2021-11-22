package io.github.alanrb.opentv.di

import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.presentation.details.VideoDetailsViewModel
import io.github.alanrb.opentv.presentation.main.MainViewModel
import io.github.alanrb.opentv.presentation.search.AppSearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

val appModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { (movie: MovieModel) -> VideoDetailsViewModel(movie, get()) }
    viewModel { AppSearchViewModel(get()) }
}