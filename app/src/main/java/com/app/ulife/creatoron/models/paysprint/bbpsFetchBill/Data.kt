/*
 * *
 *  * Created by Prady on 4/25/24, 5:09 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/25/24, 5:09 PM
 *
 */

package com.app.ulife.creatoron.models.paysprint.bbpsFetchBill

data class Data(
    val amount: String,
    val bill_fetch: BillFetch,
    val duedate: String,
    val message: String,
    val name: String,
    val response_code: Int,
    val status: Boolean
)