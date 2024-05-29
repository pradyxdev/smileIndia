/*
 * *
 *  * Created by Prady on 4/6/24, 10:27 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 10:27 AM
 *
 */

package com.app.ulife.creatoron.models.orderHistory

data class OrderHistoryReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val orderno: String,
    val userid: String
)