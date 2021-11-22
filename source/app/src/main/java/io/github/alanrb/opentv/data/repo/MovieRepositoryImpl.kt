package io.github.alanrb.opentv.data.repo

import io.github.alanrb.opentv.data.api.*
import io.github.alanrb.opentv.data.db.dao.MovieDao
import io.github.alanrb.opentv.data.db.dto.MovieDbDto
import io.github.alanrb.opentv.domain.entities.*
import io.github.alanrb.opentv.domain.repo.MovieRepository

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class MovieRepositoryImpl(
    private val trakt: TraktService,
    private val tmdb: TmdbService,
    private val movieDao: MovieDao
) : MovieRepository() {

    override suspend fun searchMovie(term: String): Result<List<MovieModel>> {
        val apiResult = trakt.searchMovie(term)
        return if (apiResult.isSuccessful) {
            Result.Success(apiResult.body()?.map { it.movie } ?: listOf())
        } else {
            NetworkFailure.NotFound
        }
    }

    override suspend fun searchShow(term: String): Result<List<ShowModel>> {
        val apiResult = trakt.searchShow(term)
        return if (apiResult.isSuccessful) {
            Result.Success(apiResult.body()?.map { it.show } ?: listOf())
        } else {
            NetworkFailure.NotFound
        }
    }

    override suspend fun getTrending(): List<MovieModel> {
        val popularApiResult = trakt.trending()
        return if (popularApiResult.isSuccessful) {
            val moviesResult = arrayListOf<MovieModel>()
            popularApiResult.body()?.forEach { trending ->
                trending.movie.ids?.let { id ->
                    val localDto = movieDao.get(id.trakt)
                    if (localDto != null) {
                        trending.movie.backgroundImageUrl = localDto.backgroundImageUrl
                        trending.movie.cardImageUrl = localDto.cardImageUrl
                        moviesResult.add(trending.movie)
                    } else {
                        val imagesApi = tmdb.getMovieImages(id.tmdb)
                        trending.movie.backgroundImageUrl =
                            "$BG_IMAGE_PREFIX${imagesApi.body()?.backdrops?.first()?.file_path}"
                        trending.movie.cardImageUrl =
                            "$IMAGE_PREFIX${imagesApi.body()?.posters?.first()?.file_path}"
                        moviesResult.add(trending.movie)
                    }
                }
            }
            val localData = moviesResult.map {
                it.mapDto()
            }
            movieDao.insertAll(localData)
            moviesResult
        } else {
            listOf()
        }
    }

    override suspend fun getPopular(): List<MovieModel> {
        val popularApiResult = trakt.popular()
        return if (popularApiResult.isSuccessful) {
            val moviesResult = arrayListOf<MovieModel>()
            popularApiResult.body()?.forEach { movie ->
                movie.ids?.let { id ->
                    val localDto = movieDao.get(id.trakt)
                    if (localDto != null) {
                        movie.backgroundImageUrl = localDto.backgroundImageUrl
                        movie.cardImageUrl = localDto.cardImageUrl
                        moviesResult.add(movie)
                    } else {
                        val imagesApi = tmdb.getMovieImages(id.tmdb)
                        movie.backgroundImageUrl =
                            "$BG_IMAGE_PREFIX${imagesApi.body()?.backdrops?.first()?.file_path}"
                        movie.cardImageUrl =
                            "$IMAGE_PREFIX${imagesApi.body()?.posters?.first()?.file_path}"
                        moviesResult.add(movie)
                    }
                }
            }
            val localData = moviesResult.map {
                it.mapDto()
            }
            movieDao.insertAll(localData)
            moviesResult
        } else {
            listOf()
        }
    }

    override suspend fun getTrendingShow(): List<ShowModel> {
        val trendingShowApiResult = trakt.trendingShow()
        return if (trendingShowApiResult.isSuccessful) {
            val moviesResult = arrayListOf<ShowModel>()
            trendingShowApiResult.body()?.forEach { trending ->
                trending.show.ids?.let { id ->
                    val imagesApi = tmdb.getShowImages(id.tmdb)
                    trending.show.backgroundImageUrl =
                        "$BG_IMAGE_PREFIX${imagesApi.body()?.backdrops?.firstOrNull()?.file_path}"
                    trending.show.cardImageUrl =
                        "$IMAGE_PREFIX${imagesApi.body()?.posters?.firstOrNull()?.file_path}"
                    moviesResult.add(trending.show)
                }
            }
            moviesResult
        } else {
            listOf()
        }
    }

    override suspend fun getSummary(slug: String): Result<MovieModel> {
        val apiResponse = trakt.movieSummary(slug)
        return if (apiResponse.isSuccessful) {
            val model = apiResponse.body()
            Result.Success(model)
        } else {
            NetworkFailure.NotFound
        }
    }

    override suspend fun relatedMovies(slug: String): Result<List<MovieModel>> {
        val apiResponse = trakt.relatedMovies(slug)
        return if (apiResponse.isSuccessful) {
            val moviesResult = arrayListOf<MovieModel>()

            apiResponse.body()?.forEach { movie ->
                movie.ids?.let { id ->
                    val localDto = movieDao.get(id.trakt)
                    if (localDto != null) {
                        movie.backgroundImageUrl = localDto.backgroundImageUrl
                        movie.cardImageUrl = localDto.cardImageUrl
                        moviesResult.add(movie)
                    } else {
                        val imagesApi = tmdb.getMovieImages(id.tmdb)
                        if (imagesApi.isSuccessful) {
                            movie.backgroundImageUrl =
                                "$BG_IMAGE_PREFIX${imagesApi.body()?.backdrops?.firstOrNull()?.file_path}"
                            movie.cardImageUrl =
                                "$IMAGE_PREFIX${imagesApi.body()?.posters?.firstOrNull()?.file_path}"
                            moviesResult.add(movie)
                        }
                    }
                }
            }
            val localData = moviesResult.map {
                it.mapDto()
            }
            movieDao.insertAll(localData)
            Result.Success(moviesResult)
        } else {
            NetworkFailure.NotFound
        }
    }

    override suspend fun getMovieWatchProviders(
        tmdbId: Long,
        region: String
    ): Result<List<WatchProviderModel>> {
        val apiResponse = tmdb.getMovieWatchProviders(tmdbId)
        return if (apiResponse.isSuccessful) {
            val response = apiResponse.body()!!.results[region]
            Result.Success(response?.flatrate ?: listOf())
        } else {
            NetworkFailure.NotFound
        }
    }

    private suspend fun loadMovieInfo(movie: MovieModel): MovieModel? {
        movie.ids?.let { id ->
            val localDto = movieDao.get(id.trakt)
            return if (localDto != null) {
                movie.backgroundImageUrl = localDto.backgroundImageUrl
                movie.cardImageUrl = localDto.cardImageUrl
                movie
            } else {
                val imagesApi = tmdb.getMovieImages(id.tmdb)
                movie.backgroundImageUrl =
                    "$BG_IMAGE_PREFIX${imagesApi.body()?.backdrops?.first()?.file_path}"
                movie.cardImageUrl =
                    "$IMAGE_PREFIX${imagesApi.body()?.posters?.first()?.file_path}"
                movie
            }
        }
        return null
    }

    private fun MovieModel.mapDto() = MovieDbDto(
        id = this.id,
        title = this.title ?: "",
        overview = this.overview,
        backgroundImageUrl = this.backgroundImageUrl,
        cardImageUrl = this.cardImageUrl,
        videoUrl = this.videoUrl,
        studio = this.studio,
        ids = this.ids,
        released = this.released,
        year = this.year,
        runtime = this.runtime,
        certification = this.certification
    )

}