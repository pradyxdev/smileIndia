/*
 * *
 *  * Created by Prady on 4/4/24, 5:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 5:21 PM
 *
 */

package com.app.ulife.creator.models.getCity

data class GetCityReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val StateId: String
)