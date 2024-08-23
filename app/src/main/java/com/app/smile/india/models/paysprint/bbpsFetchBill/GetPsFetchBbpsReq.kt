/*
 * *
 *  * Created by Prady on 4/23/24, 11:13 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 11:13 AM
 *
 */

package com.app.smile.india.models.paysprint.bbpsFetchBill

data class GetPsFetchBbpsReq(
    val ad1: String,
    val ad2: String,
    val ad3: String,
    val canumber: String,
    val mode: String,
    val `operator`: String,
    val userid: String
)