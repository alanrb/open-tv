package io.github.alanrb.opentv.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.alanrb.opentv.data.db.dao.MovieDao
import io.github.alanrb.opentv.data.db.dto.MovieDbDto

/**
 * Created by Tuong (Alan) on 6/16/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

@Database(
    entities = [MovieDbDto::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}