/*
 * *
 *  * Created by Prady on 4/23/24, 6:53 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 6:53 PM
 *
 */

package com.app.ulife.creatoron.models.paysprint.fastag.psFetchFastag

data class BillFetch(
    val acceptPartPay: Boolean,
    val acceptPayment: Boolean,
    val billAmount: String,
    val billdate: Any,
    val billnetamount: String,
    val cellNumber: String,
    val dueDate: String,
    val userName: String
)