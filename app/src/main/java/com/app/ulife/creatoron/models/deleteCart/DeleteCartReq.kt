/*
 * *
 *  * Created by Prady on 4/4/24, 4:24 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 4:24 PM
 *
 */

package com.app.ulife.creatoron.models.deleteCart

data class DeleteCartReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val id: String,
    val userid: String
)