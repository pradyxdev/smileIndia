/*
 * *
 *  * Created by Prady on 4/3/24, 7:07 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/3/24, 7:07 PM
 *
 */

package com.app.ulife.creatoron.models.getCat

data class GetCategoryRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)

data class Data(
    val CategoryImage: String,
    val CategoryName: String,
    val Id: Int,
    val catid: Int,
    val img: String,
    val name: String
)