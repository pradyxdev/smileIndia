/*
 * *
 *  * Created by Prady on 4/10/24, 7:11 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 7:11 PM
 *
 */

package com.app.ulife.creator.models.fundsManage.add

data class AddFundsReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val ReqwalleyType: String,
    val Type: String,
    val amount: String,
    val chainid: String,
    val coinprice: String,
    val dollerprice: String,
    val fromaccount: String,
    val reciept: String,
    val remark: String,
    val reqtype: String,
    val toaccount: String,
    val transactionid: String,
    val transfercoin: String,
    val userid: String,
    val RequestFrom: String
)