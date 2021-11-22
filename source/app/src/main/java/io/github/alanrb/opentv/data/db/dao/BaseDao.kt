package io.github.alanrb.opentv.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

/**
 * Created by Tuong (Alan) on 6/16/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

interface BaseDao<Entity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<Entity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Entity)

    @Delete
    suspend fun delete(entity: Entity)
}