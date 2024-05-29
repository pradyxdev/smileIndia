/*
 * *
 *  * Created by Prady on 4/5/24, 3:32 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/5/24, 3:32 PM
 *
 */

package com.app.ulife.creatoron.models.checkout

data class CheckoutOrderReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val BillingAddress: String,
    val PayMode: String,
    val UserId: String
)