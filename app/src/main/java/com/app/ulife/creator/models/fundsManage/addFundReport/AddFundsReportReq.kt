/*
 * *
 *  * Created by Prady on 4/12/24, 10:42 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 10:42 AM
 *
 */

package com.app.ulife.creator.models.fundsManage.addFundReport

data class AddFundsReportReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val Type: String,
    val from: String,
    val status: String,
    val to: String,
    val userid: String
)