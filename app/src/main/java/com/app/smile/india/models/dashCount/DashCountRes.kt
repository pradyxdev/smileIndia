/*
 * *
 *  * Created by Prady on 4/4/24, 11:08 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 11:08 AM
 *
 */

package com.app.smile.india.models.dashCount

data class DashCountRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)