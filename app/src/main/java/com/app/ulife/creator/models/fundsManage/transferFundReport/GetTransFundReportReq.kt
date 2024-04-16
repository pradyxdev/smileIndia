/*
 * *
 *  * Created by Prady on 4/13/24, 6:59 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 6:59 PM
 *
 */

package com.app.ulife.creator.models.fundsManage.transferFundReport

data class GetTransFundReportReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val Type: String,
    val WalletType: String,
    val from: String,
    val to: String,
    val touserid: String,
    val userid: String
)