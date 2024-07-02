/*
 * *
 *  * Created by Prady on 4/23/24, 11:13 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 11:13 AM
 *
 */

package com.app.ulife.creatoron.models.paysprint.lpg.payLpgBill

data class GetPsLpgPayBillReq(
    val userid: String,
    val canumber: String,
    val amount: Double,
    val latitude: String,
    val longitude: String,
    val `operator`: Int,
    val Circle: String,
    val PlanDescription: String,
    val ad1: String,
    val ad2: String,
    val ad3: String,
    val ad4: String,
    val OperatorName: String,
)