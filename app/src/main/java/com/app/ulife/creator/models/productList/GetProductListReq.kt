/*
 * *
 *  * Created by Prady on 4/4/24, 1:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 1:03 PM
 *
 */

package com.app.ulife.creator.models.productList

data class GetProductListReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val id: String,
    val name: String,
    val type: String
)