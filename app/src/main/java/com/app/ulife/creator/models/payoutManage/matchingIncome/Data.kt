/*
 * *
 *  * Created by Prady on 4/15/24, 3:39 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 3:39 PM
 *
 */

package com.app.ulife.creator.models.payoutManage.matchingIncome

data class Data(
    val BinaryIncome: Int,
    val EntryDate: String,
    val FromDate: String,
    val LeftcarryPV: Int,
    val Pair: Int,
    val RightCarryPv: Int,
    val ShoppingCharge: Any,
    val ToDate: String,
    val TotalLeft: Int,
    val TotalRight: Int,
    val UseLeft: Int,
    val UseRight: Int,
    val UserName: String,
    val admincharge: Int,
    val paybleamount: Int,
    val tdscharge: Int,
    val userid: String
)