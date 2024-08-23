/*
 * *
 *  * Created by Prady on 4/5/24, 3:32 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/5/24, 3:32 PM
 *
 */

package com.app.smile.india.models.checkout

data class CheckoutOrderReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val paymentmode: String,
    val UserId: String,
    val wallettype: String,
    val CheckoutId: String,
//    val transactionpassword: String
)