/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creator.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DummyDao {

    /**
     * It returns a LiveData object of List of Dummy objects
     */
    @Query("SELECT * from tb_name")
    fun getDummy(): LiveData<List<Dummy>>

    @Insert
    suspend fun insertDummy(dummy: Dummy)
}