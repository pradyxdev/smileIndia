/*
 * *
 *  * Created by Prady on 4/13/24, 5:16 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 5:16 PM
 *
 */

package com.app.ulife.creator.models.fetchBill

data class Table(
    val account: String,
    val agentid: String,
    val amount: String,
    val bal: String,
    val billdate: String,
    val billnumber: String,
    val bilperiod: String,
    val createdOn: String,
    val customername: String,
    val dueamount: String,
    val duedate: String,
    val errorcode: String,
    val fetchBillID: String,
    val isRefundStatusShow: String,
    val mobile: String,
    val msg: String,
    val name: String,
    val opid: String,
    val orderid: String,
    val rechargeStatus: String,
    val rechargeamount: String,
    val rpid: String,
    val serviceName: String,
    val status: String
)