/*
 * *
 *  * Created by Prady on 4/23/24, 6:53 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 6:53 PM
 *
 */

package com.app.ulife.creator.models.paysprint.fastag.psFetchFastag

data class Data(
    val amount: String,
    val bill_fetch: BillFetch,
    val duedate: String,
    val message: String,
    val name: String,
    val response_code: Int,
    val status: Boolean
)