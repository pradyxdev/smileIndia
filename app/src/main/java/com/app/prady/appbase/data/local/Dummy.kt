/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.prady.appbase.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An entity is a table in the database.
 *
 * The @Entity annotation is used to mark a class as an entity.
 *
 * The tableName property is used to tell the database the name of the table to use for the entity.
 *
 * The @PrimaryKey annotation is used to mark a field as the primary key for the entity.
 *
 * The autoGenerate property is used to tell the database to generate a value for the primary key field
 * when an entity is inserted.
 *
 * The data class is used to create a POJO with the fields marked with the @ColumnInfo annotation.
 */
@Entity(tableName = "tb_name")
data class Dummy(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String
)
