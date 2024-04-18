/*
 * *
 *  * Created by Prady on 4/18/24, 4:19 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/18/24, 4:19 PM
 *
 */

package com.app.ulife.creator.models.payoutManage.ldc.report

data class GetLdcReportReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val closingid: String,
    val userid: String
)