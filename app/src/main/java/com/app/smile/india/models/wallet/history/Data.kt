/*
 * *
 *  * Created by Prady on 4/6/24, 11:21 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 11:21 AM
 *
 */

package com.app.smile.india.models.wallet.history

data class Data(
    val CrAmount: Double,
    val DrAmount: Double,
    val EntryDate: String,
    val Id: Int,
    val Remark: String,
    val TransactionType: String
)