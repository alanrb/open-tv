package io.github.alanrb.opentv.data.api

import io.github.alanrb.opentv.data.api.dto.PeopleResponse
import io.github.alanrb.opentv.data.api.dto.ShowResponse
import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.domain.entities.TrendingMovieModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

interface TraktService {

    @GET("search/movie")
    suspend fun searchMovie(@Query("query") term: String): Response<List<TrendingMovieModel>>

    @GET("search/show")
    suspend fun searchShow(@Query("query") term: String): Response<List<ShowResponse>>

    @GET("movies/trending?extended=full")
    suspend fun trending(): Response<List<TrendingMovieModel>>

    @GET("shows/trending?extended=full")
    suspend fun trendingShow(): Response<List<ShowResponse>>

    @GET("movies/popular?extended=full")
    suspend fun popular(): Response<List<MovieModel>>

    @GET("movies/{movie_id}?extended=full")
    suspend fun movieSummary(@Path("movie_id") movieId: String): Response<MovieModel>

    @GET("movies/{movie_id}/related?extended=full")
    suspend fun relatedMovies(@Path("movie_id") movieId: String): Response<List<MovieModel>>

    @GET("movies/{movie_id}/people")
    suspend fun moviePeople(@Path("movie_id") movieId: String): Response<PeopleResponse>
}