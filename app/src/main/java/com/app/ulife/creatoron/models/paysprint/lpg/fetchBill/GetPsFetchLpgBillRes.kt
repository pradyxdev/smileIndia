/*
 * *
 *  * Created by Prady on 5/29/24, 11:24 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 5/29/24, 11:24 AM
 *
 */

package com.app.ulife.creatoron.models.paysprint.lpg.fetchBill

data class GetPsFetchLpgBillRes(
    val `data`: Data,
    val message: String,
    val requestId: Any,
    val status: Boolean
)