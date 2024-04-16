/*
 * *
 *  * Created by Prady on 4/15/24, 1:24 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 1:24 PM
 *
 */

package com.app.ulife.creator.models.withdrawManage.withdrawalReq

data class StartWithdrawalReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val AccHolderName: String,
    val AccNo: String,
    val BankName: String,
    val IFSC: String,
    val ShoppingCharge: String,
    val Tax: String,
    val TransPswd: String,
    val admincharge: String,
    val amount: String,
    val chainid: String,
    val coinprice: String,
    val dollerprice: String,
    val netamount: String,
    val toaccount: String,
    val transfercoin: String,
    val userid: String
)