package io.github.alanrb.opentv.domain.entities

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

data class TrendingMovieModel(
    val tagline: String,
    val overview: String,
    val movie: MovieModel
)