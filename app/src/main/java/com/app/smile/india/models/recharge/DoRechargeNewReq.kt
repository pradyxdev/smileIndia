/*
 * *
 *  * Created by Prady on 4/6/24, 4:52 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 4:52 PM
 *
 */

package com.app.smile.india.models.recharge

data class DoRechargeNewReq(
    val member_Id: String,
    val mobileNo: String,
    val operatorID: String,
    val operatorName: String,
    val operatorType: String,
    val rechargeAmount: String,
    val transactionPass: String,
    val walletAmount: String,
    val walletType: String,
    val planId: String,
    val FetchBillID: String,
)