/*
 * *
 *  * Created by Prady on 4/15/24, 4:15 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 4:15 PM
 *
 */

package com.app.smile.india.models.payoutManage.rechargeIncome

data class GetRechargeIncomeReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val from: String,
    val lvl: String,
    val to: String,
    val type: String,
    val userid: String
)