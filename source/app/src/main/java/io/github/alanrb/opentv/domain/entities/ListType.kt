package io.github.alanrb.opentv.domain.entities

import io.github.alanrb.opentv.R

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

enum class ListType(val key: Int, val title: Int, val icon: Int) {
    TRENDING(0, R.string.title_trending, R.drawable.ic_bubble_chart),
    POPULAR(1, R.string.title_popular, R.drawable.ic_stars),
    TRENDING_SHOW(2, R.string.title_trending_show, R.drawable.ic_thumb_up),
    PREFERENCES(3, R.string.title_preferences, R.drawable.ic_settings);

    companion object {
        fun parse(key: Int): ListType = values().first { it.key == key }
    }

}