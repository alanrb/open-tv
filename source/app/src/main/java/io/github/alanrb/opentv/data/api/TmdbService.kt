package io.github.alanrb.opentv.data.api

import io.github.alanrb.opentv.data.api.dto.TmdbImages
import io.github.alanrb.opentv.data.api.dto.WatchProviderResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

interface TmdbService {
    companion object {
        private const val API = ""
    }

    @GET("movie/{movie_id}/images?api_key=$API")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Long
    ): Response<TmdbImages>

    @GET("tv/{show_id}/images?api_key=$API")
    suspend fun getShowImages(
        @Path("show_id") showId: Long
    ): Response<TmdbImages>

    @GET("movie/{movie_id}/watch/providers?api_key=$API&language=en-US&watch_region=US")
    suspend fun getMovieWatchProviders(
        @Path("movie_id") movieId: Long
    ): Response<WatchProviderResponse>
}

const val IMAGE_PREFIX = "https://image.tmdb.org/t/p/w200"
const val BG_IMAGE_PREFIX = "https://image.tmdb.org/t/p/original"