package io.github.alanrb.opentv.domain.repo

import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.domain.entities.Result
import io.github.alanrb.opentv.domain.entities.ShowModel
import io.github.alanrb.opentv.domain.entities.WatchProviderModel

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

abstract class MovieRepository {
    abstract suspend fun searchMovie(term: String): Result<List<MovieModel>>
    abstract suspend fun searchShow(term: String): Result<List<ShowModel>>
    abstract suspend fun getTrending(): List<MovieModel>
    abstract suspend fun getPopular(): List<MovieModel>
    abstract suspend fun getTrendingShow(): List<ShowModel>
    abstract suspend fun getSummary(slug: String): Result<MovieModel>
    abstract suspend fun relatedMovies(slug: String): Result<List<MovieModel>>
    abstract suspend fun getMovieWatchProviders(
        tmdbId: Long,
        region: String
    ): Result<List<WatchProviderModel>>
}