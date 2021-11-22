package io.github.alanrb.opentv.data.api.dto

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

data class TmdbImages(
    val id: Long,
    val backdrops: List<Poster>,
    val posters: List<Poster>
) {
    data class Poster(
        val aspect_ratio: Double,
        val file_path: String,
        val height: Int,
        val vote_average: Double,
        val vote_count: Int,
        val width: Int,
    )
}