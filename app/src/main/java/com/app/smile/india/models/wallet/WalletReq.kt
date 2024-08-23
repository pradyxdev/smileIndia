/*
 * *
 *  * Created by Prady on 4/6/24, 11:20 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 11:20 AM
 *
 */

package com.app.smile.india.models.wallet

data class WalletReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val datatype: String,
    val transactiontype: String,
    val userid: String,
    val wallettype: String
)