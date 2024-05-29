/*
 * *
 *  * Created by Prady on 4/12/24, 1:43 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 1:43 PM
 *
 */

package com.app.ulife.creatoron.models.fundsManage.transferFund

data class GetTransferFundsReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val P2Ptype: String,
    val amount: String,
    val touserid: String,
    val type: String,
    val userid: String
)