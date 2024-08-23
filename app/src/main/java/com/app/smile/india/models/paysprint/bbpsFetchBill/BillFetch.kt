/*
 * *
 *  * Created by Prady on 4/25/24, 5:09 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/25/24, 5:09 PM
 *
 */

package com.app.smile.india.models.paysprint.bbpsFetchBill

data class BillFetch(
    val acceptPartPay: Boolean,
    val acceptPayment: Boolean,
    val billAmount: String,
    val billdate: String,
    val billnetamount: String,
    val cellNumber: String,
    val dueDate: String,
    val userName: String
)