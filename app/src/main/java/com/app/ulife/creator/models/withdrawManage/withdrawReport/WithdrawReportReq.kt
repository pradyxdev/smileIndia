/*
 * *
 *  * Created by Prady on 4/15/24, 2:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 2:21 PM
 *
 */

package com.app.ulife.creator.models.withdrawManage.withdrawReport

data class WithdrawReportReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val from: String,
    val status: String,
    val to: String,
    val userid: String
)