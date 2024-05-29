/*
 * *
 *  * Created by Prady on 4/12/24, 4:54 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 4:54 PM
 *
 */

package com.app.ulife.creatoron.models.epinManage.epinReport

data class EPinReportReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val from: String,
    val planid: String,
    val status: String,
    val to: String,
    val userid: String
)