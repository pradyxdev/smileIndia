/*
 * *
 *  * Created by Prady on 4/4/24, 3:15 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 3:15 PM
 *
 */

package com.app.ulife.creatoron.models.cart

data class GetCartRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)

data class Data(
    val Count: Int,
    val ProductId: Int,
    val ProductImage: String,
    val ProductMrp: Double,
    val ProductName: String,
    val Qty: Int,
    val SubTotal: Int,
    val SP: Double,
    val Total: Int,
    val UserId: String,
    val id: Int
)