/*
 * *
 *  * Created by Prady on 4/13/24, 5:16 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 5:16 PM
 *
 */

package com.app.smile.india.models.fetchBill

data class FetchBillRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)