/*
 * *
 *  * Created by Prady on 4/9/24, 6:35 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 3:40 PM
 *
 */

package com.app.ulife.creatoron.models.bbpsRecharge.operator

data class GetOperatorReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val Type: String
)