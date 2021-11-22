package io.github.alanrb.opentv.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Tuong (Alan) on 6/16/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

@Parcelize
data class ShowModel(
    val title: String,
    var overview: String? = null,
    var backgroundImageUrl: String? = null,
    var cardImageUrl: String?,
    var videoUrl: String?,
    var network: String? = "DC comic",
    var ids: IdModel? = null,

    @SerializedName("first_aired")
    val firstAired: String,

    @SerializedName("aired_episodes")
    val airedEpisodes: Int,
    val year: String,
    var runtime: Int? = null,
    val certification: String
) : Parcelable