/*
 * *
 *  * Created by Prady on 5/10/24, 6:44 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 5/10/24, 6:44 PM
 *
 */

package com.app.smile.india.models.paysprint.lpg.fetchBill

data class GetPsFetchLpgBillReq(
    val userid: String,
    val operator: Int,
    val canumber: String,
    val Ad1: String,
    val Ad2: String,
    val Ad3: String,
    val Ad4: String,
    val latitude: String,
    val longitude: String,
    val OperatorName: String,
    val Circle: String,
    val PlanDescription: String
)