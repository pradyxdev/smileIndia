/*
 * *
 *  * Created by Prady on 4/12/24, 6:07 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 6:07 PM
 *
 */

package com.app.smile.india.models.epinManage.epinRequest

data class EpinRequestReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val userid: String,
    val packageid: String,
    val amount: String,
    val noofepin: String,
    val totalamount: String,
    val transactionno: String,
    val remark: String,
    val reciept: String,
    val RequestFrom: String,
)