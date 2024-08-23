/*
 * *
 *  * Created by Prady on 4/4/24, 5:09 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 5:09 PM
 *
 */

package com.app.smile.india.models.getState

data class GetStateRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)

data class Data(
    val Id: Int,
    val StateName: String
)