package io.github.alanrb.opentv.data.db.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.alanrb.opentv.domain.entities.IdModel

/**
 * Created by Tuong (Alan) on 6/16/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

@Entity(tableName = "movie")
data class MovieDbDto(
    @PrimaryKey
    var id: Long,
    var title: String,
    var overview: String?,
    var backgroundImageUrl: String?,
    var cardImageUrl: String?,
    var videoUrl: String?,
    var studio: String?,
    @Embedded
    var ids: IdModel? = null,
    var released: String? = null,
    var year: String? = null,
    var runtime: Int? = null,
    var certification: String? = null
)