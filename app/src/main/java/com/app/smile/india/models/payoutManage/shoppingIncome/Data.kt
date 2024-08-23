/*
 * *
 *  * Created by Prady on 4/15/24, 4:00 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 4:00 PM
 *
 */

package com.app.smile.india.models.payoutManage.shoppingIncome

data class Data(
    val FUserName: String,
    val Lvl: Int,
    val Remark: String,
    val ShoppingCharge: Any,
    val UserName: String,
    val admincharge: Int,
    val directincome: Double,
    val entrydate: String,
    val fromuserid: String,
    val id: Int,
    val paybleamount: Double,
    val tdscharge: Int,
    val userid: String
)