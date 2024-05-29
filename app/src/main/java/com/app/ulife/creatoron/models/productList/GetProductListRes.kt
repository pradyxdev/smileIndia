/*
 * *
 *  * Created by Prady on 4/4/24, 1:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 1:03 PM
 *
 */

package com.app.ulife.creatoron.models.productList

data class GetProductListRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)

data class Data(
    val bv: Double,
    val desc: String,
    val img: String,
    val mrp: Double,
    val name: String,
    val prodid: Int,
    val sp: Double,
    val type: String,
    val AvailableStock: Int
)