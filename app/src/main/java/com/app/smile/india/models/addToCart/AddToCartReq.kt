/*
 * *
 *  * Created by Prady on 4/4/24, 2:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 2:03 PM
 *
 */

package com.app.smile.india.models.addToCart

data class AddToCartReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val productid: String,
    val qty: String,
    val userid: String
)