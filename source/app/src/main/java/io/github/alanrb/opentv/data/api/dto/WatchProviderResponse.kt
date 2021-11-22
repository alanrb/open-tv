package io.github.alanrb.opentv.data.api.dto

import io.github.alanrb.opentv.domain.entities.WatchProviderModel

/**
 * Created by Tuong (Alan) on 6/15/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

data class WatchProviderResponse(
    val results: Map<String, WatchProviderMethod>
) {
    data class WatchProviderMethod(
        val rent: List<WatchProviderModel>,
        val buy: List<WatchProviderModel>,
        val flatrate: List<WatchProviderModel>
    )
}