package io.github.alanrb.opentv.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.domain.entities.ShowModel
import io.github.alanrb.opentv.domain.repo.MovieRepository
import kotlinx.coroutines.launch

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class MainViewModel(
    private val movieRepo: MovieRepository
) : ViewModel() {
    val popularResult = MutableLiveData<List<MovieModel>>()
    val trendingResult = MutableLiveData<List<MovieModel>>()
    val trendingShowResult = MutableLiveData<List<ShowModel>>()

    fun get() {
        viewModelScope.launch {
            trendingResult.value = movieRepo.getTrending()
            popularResult.value = movieRepo.getPopular()
            trendingShowResult.value = movieRepo.getTrendingShow()
        }
    }

}