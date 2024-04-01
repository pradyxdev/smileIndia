/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creator.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/* The DummyDatabase class is a RoomDatabase that contains a single table, Dummy */
@Database(entities = [Dummy::class], version = 1, exportSchema = false)
abstract class DummyDatabase : RoomDatabase() {

    /**
     * It creates an instance of the DummyDatabase class
     */
    abstract fun dummyDao(): DummyDao

    /* A singleton pattern. */
    companion object {
        private var INSTANCE: DummyDatabase? = null
        fun getDatabase(context: Context): DummyDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        DummyDatabase::class.java,
                        "database_name"
                    )
//                      .createFromAsset("/assets/local.db")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}