package io.github.alanrb.opentv.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Tuong (Alan) on 6/15/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

@Parcelize
data class WatchProviderModel(
    @SerializedName("display_priority")
    val displayPriority: Int,

    @SerializedName("logo_path")
    val logoPath: String,

    @SerializedName("provider_name")
    val providerName: String,

    @SerializedName("provider_id")
    val providerId: Int
) : Parcelable