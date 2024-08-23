/*
 * *
 *  * Created by Prady on 4/18/24, 4:22 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/18/24, 4:22 PM
 *
 */

package com.app.smile.india.models.payoutManage.ldc.report

data class Data(
    val AdminCharge: Double,
    val Bussiness: Double,
    val EntryDate: String,
    val FromDate: String,
    val Id: Int,
    val Income: Double,
    val Payable: Double,
    val Per: Int,
    val TDSCharge: Double,
    val ToDate: String,
    val UserId: String
)