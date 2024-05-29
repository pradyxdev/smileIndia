/*
 * *
 *  * Created by Prady on 4/6/24, 2:07 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 2:07 PM
 *
 */

package com.app.ulife.creatoron.models.orderItems

data class Data(
    val BV: Int,
    val Id: Int,
    val ProductId: Int,
    val ProductImage: String,
    val ProductMrp: Int,
    val ProductName: String,
    val Qty: Int,
    val SP: Int,
    val SubTotal: Double,
    val TotalAmount: Int,
    val TotalGST: Double
)