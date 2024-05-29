/*
 * *
 *  * Created by Prady on 4/15/24, 3:24 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 3:24 PM
 *
 */

package com.app.ulife.creatoron.models.payoutManage.directIncome

data class Data(
    val FUserName: String,
    val Remark: String,
    val ShoppingCharge: Any,
    val UserName: String,
    val admincharge: Int,
    val directincome: Int,
    val entrydate: String,
    val fromuserid: String,
    val id: Int,
    val paybleamount: Int,
    val tdscharge: Int,
    val userid: String
)