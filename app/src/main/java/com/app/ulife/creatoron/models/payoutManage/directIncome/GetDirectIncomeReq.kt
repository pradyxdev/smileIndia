/*
 * *
 *  * Created by Prady on 4/15/24, 3:16 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 3:16 PM
 *
 */

package com.app.ulife.creatoron.models.payoutManage.directIncome

data class GetDirectIncomeReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val from: String,
    val to: String,
    val userid: String
)