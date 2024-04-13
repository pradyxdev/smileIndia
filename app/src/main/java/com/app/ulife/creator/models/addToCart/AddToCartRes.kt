/*
 * *
 *  * Created by Prady on 4/4/24, 2:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 2:03 PM
 *
 */

package com.app.ulife.creator.models.addToCart

data class AddToCartRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)

data class Data(
    val id: Int,
    val msg: String
)