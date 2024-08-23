/*
 * *
 *  * Created by Prady on 4/25/24, 4:23 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/25/24, 4:23 PM
 *
 */

package com.app.smile.india.models.paysprint.dthHlr

data class Info(
    val balance: String,
    val customerName: String,
    val monthlyRecharge: String,
    val nextRechargeDate: String,
    val planname: String,
    val status: String
)