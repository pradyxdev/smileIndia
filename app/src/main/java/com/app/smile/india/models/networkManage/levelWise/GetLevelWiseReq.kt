/*
 * *
 *  * Created by Prady on 4/10/24, 5:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:21 PM
 *
 */

package com.app.smile.india.models.networkManage.levelWise

data class GetLevelWiseReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val from: String,
    val status: String,
    val to: String,
    val userid: String
)