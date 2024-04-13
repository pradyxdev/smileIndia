/*
 * *
 *  * Created by Prady on 4/6/24, 2:07 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 2:07 PM
 *
 */

package com.app.ulife.creator.models.orderItems

data class OrderItemsRes(
    val `data`: MutableList<Data>,
    val message: String,
    val status: Boolean
)