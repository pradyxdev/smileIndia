/*
 * *
 *  * Created by Prady on 4/15/24, 7:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 7:21 PM
 *
 */

package com.app.smile.india.models.epinManage.epinReqReport

data class Data(
    val Amount: Int,
    val ApproveDate: String,
    val EntryDate: String,
    val Id: Int,
    val NoOfEPin: Int,
    val PlanName: String,
    val Reciept: String,
    val Status: String,
    val TotalAmount: Int,
    val TransactionNo: String,
    val UserId: String,
    val UserName: String
)