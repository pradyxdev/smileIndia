/*
 * *
 *  * Created by Prady on 4/15/24, 4:18 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 4:18 PM
 *
 */

package com.app.ulife.creator.models.payoutManage.rechargeIncome

data class Data(
    val Lvl: Int,
    val PackageAmount: Double,
    val Remark: String,
    val ShoppingCharge: Double,
    val URId: String,
    val admincharge: Int,
    val adminper: Int,
    val did: String,
    val directincome: Double,
    val entrydate: String,
    val fromuserid: String,
    val id: Int,
    val paybleamount: Double,
    val tdscharge: Double,
    val tdsper: Int,
    val userid: String
)