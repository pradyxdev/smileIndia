/*
 * *
 *  * Created by Prady on 4/6/24, 1:07 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 1:07 PM
 *
 */

package com.app.smile.india.models.orderHistory

data class OrderHistoryRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)