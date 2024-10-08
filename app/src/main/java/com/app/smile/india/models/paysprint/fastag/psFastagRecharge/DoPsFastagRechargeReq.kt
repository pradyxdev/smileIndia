/*
 * *
 *  * Created by Prady on 4/23/24, 12:15 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 12:15 PM
 *
 */

package com.app.smile.india.models.paysprint.fastag.psFastagRecharge

data class DoPsFastagRechargeReq(
    val amount: Double,
    val billfetch: Any,
    val canumber: String,
    val latitude: String,
    val longitude: String,
    val operator: Int,
    val userid: String
)