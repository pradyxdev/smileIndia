/*
 * *
 *  * Created by Prady on 4/6/24, 12:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:45 PM
 *
 */

package com.app.smile.india.models.wallet.payout

data class Data(
    val CrAmount: Double,
    val DrAmount: Int,
    val EntryDate: String,
    val Id: Int,
    val Remark: String,
    val TransactionType: String
)