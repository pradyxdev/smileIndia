/*
 * *
 *  * Created by Prady on 4/6/24, 1:07 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 1:07 PM
 *
 */

package com.app.smile.india.models.orderHistory

data class Data(
    val Amount: Double,
    val ApprovedDate: Any,
    val BillingAdd: String,
    val BillingDate: String,
    val C_Email: String,
    val C_Mobile: String,
    val C_Name: String,
    val CloseDate: Any,
    val CouponAmt: Double,
    val EntryBy: String,
    val EntryDate: String,
    val Fk_CustomerId: Int,
    val FranchiseId: String,
    val Id: Int,
    val InvNo: String,
    val IsClose: Boolean,
    val NetPayable: Double,
    val PaidAmount: Double,
    val PayMode: String,
    val PramotorStatus: String,
    val Reciept: Any,
    val SaleType: String,
    val ShopWalletAmt: Int,
    val Status: String,
    val TaxableAmt: Double,
    val TotalGst: Double,
    val TransactionId: Any,
    val UpdatedDate: Any,
    val UserId: String
)