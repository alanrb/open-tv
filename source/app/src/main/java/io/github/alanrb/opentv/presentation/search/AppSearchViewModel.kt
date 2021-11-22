package io.github.alanrb.opentv.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.domain.entities.Result
import io.github.alanrb.opentv.domain.entities.ShowModel
import io.github.alanrb.opentv.domain.repo.MovieRepository
import kotlinx.coroutines.launch

/**
 * Created by Tuong (Alan) on 6/17/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class AppSearchViewModel(val movieRepository: MovieRepository) : ViewModel() {

    val movieResult = MutableLiveData<Result<List<MovieModel>>>()
    val showResult = MutableLiveData<Result<List<ShowModel>>>()

    fun submit(term: String) {
        viewModelScope.launch {
            movieResult.value = Result.Loading
            showResult.value = Result.Loading

            movieResult.value = movieRepository.searchMovie(term)
            showResult.value = movieRepository.searchShow(term)
        }
    }
}