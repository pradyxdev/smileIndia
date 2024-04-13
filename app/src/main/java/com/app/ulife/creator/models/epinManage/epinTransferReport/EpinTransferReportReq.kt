/*
 * *
 *  * Created by Prady on 4/12/24, 6:02 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 6:02 PM
 *
 */

package com.app.ulife.creator.models.epinManage.epinTransferReport

data class EpinTransferReportReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val from: String,
    val to: String,
    val userid: String
)