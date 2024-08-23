/*
 * *
 *  * Created by Prady on 4/25/24, 5:09 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/25/24, 5:09 PM
 *
 */

package com.app.smile.india.models.paysprint.bbpsFetchBill

data class GetPsFetchBbpsRes(
    val `data`: Data,
    val message: String,
    val requestId: Any,
    val status: Boolean
)