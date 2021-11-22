package io.github.alanrb.opentv.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.alanrb.opentv.data.db.dto.MovieDbDto

/**
 * Created by Tuong (Alan) on 6/16/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

@Dao
abstract class MovieDao : BaseDao<MovieDbDto> {
    @Query("SELECT * FROM movie WHERE id=:id LIMIT 1")
    abstract suspend fun get(id: Long): MovieDbDto?

}