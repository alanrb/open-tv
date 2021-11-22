package io.github.alanrb.opentv.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */
@Parcelize
data class IdModel(
    val trakt: Long,
    val slug: String,
    val imdb: String,
    val tmdb: Long
) : Parcelable