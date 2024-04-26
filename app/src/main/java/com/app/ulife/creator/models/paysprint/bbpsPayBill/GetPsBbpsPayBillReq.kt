/*
 * *
 *  * Created by Prady on 4/23/24, 11:13 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 11:13 AM
 *
 */

package com.app.ulife.creator.models.paysprint.bbpsPayBill

data class GetPsBbpsPayBillReq(
    val userid: String,
    val canumber: String,
    val amount: Double,
    val latitude: String,
    val longitude: String,
    val mode: String,
    val `operator`: Int,
    val bill_fetch: Any,
    val Circle: String,
    val PlanDescription: String
)