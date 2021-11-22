package io.github.alanrb.opentv.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.domain.entities.Result
import io.github.alanrb.opentv.domain.repo.MovieRepository

/**
 * Created by Tuong (Alan) on 6/15/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class VideoDetailsViewModel(
    private val movie: MovieModel,
    private val movieRepository: MovieRepository
) :
    ViewModel() {

    val movieResult = liveData {
        emit(Result.Success(movie))
        emit(Result.Loading)
        movie.ids?.let { ids ->
            val result = movieRepository.getSummary(ids.slug)
            emit(result)
        }
    }

    val watchProviderResult = liveData {
        emit(Result.Loading)
        movie.ids?.let { ids ->
            val result = movieRepository.getMovieWatchProviders(ids.tmdb, "US")
            emit(result)
        }
    }

    val relatedResult = liveData {
        emit(Result.Loading)
        movie.ids?.let { ids ->
            val result = movieRepository.relatedMovies(ids.slug)
            emit(result)
        }
    }

    fun get() {
    }
}