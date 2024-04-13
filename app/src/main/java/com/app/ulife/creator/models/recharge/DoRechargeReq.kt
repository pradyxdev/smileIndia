/*
 * *
 *  * Created by Prady on 4/6/24, 3:38 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 3:38 PM
 *
 */

package com.app.ulife.creator.models.recharge

data class DoRechargeReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val WalletAmount: String,
    val RechargeAmount: String,
    val OperatorID: String,
    val OperatorName: String,
    val MobileNo: String,
    val Member_Id: String,
    val OperatorType: String,
    val WalletType: String,
    val TransactionPass: String
)