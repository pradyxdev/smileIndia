/*
 * *
 *  * Created by Prady on 4/6/24, 2:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 2:06 PM
 *
 */

package com.app.ulife.creatoron.models.orderItems

data class OrderItemsReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val orderno: String
)