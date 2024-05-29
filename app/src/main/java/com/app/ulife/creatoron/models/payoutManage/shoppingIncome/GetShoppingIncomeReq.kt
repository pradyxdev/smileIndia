/*
 * *
 *  * Created by Prady on 4/15/24, 3:57 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 3:57 PM
 *
 */

package com.app.ulife.creatoron.models.payoutManage.shoppingIncome

data class GetShoppingIncomeReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val Lvl: String,
    val from: String,
    val to: String,
    val userid: String
)