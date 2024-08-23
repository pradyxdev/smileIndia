/*
 * *
 *  * Created by Prady on 4/4/24, 5:25 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 5:25 PM
 *
 */

package com.app.smile.india.models.getCity

data class GetCityRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)

data class Data(
    val Text: String,
    val Value: Int
)