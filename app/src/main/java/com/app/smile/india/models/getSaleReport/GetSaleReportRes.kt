/*
 * *
 *  * Created by Prady on 8/17/24, 5:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/17/24, 5:03 PM
 *
 */

package com.app.smile.india.models.getSaleReport

data class GetSaleReportRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val BillingAddress: String,
        val CouponAmount: Int,
        val CouponCode: Any,
        val Discount: Double,
        val EntryBy: String,
        val EntryDate: String,
        val FStateName: Any,
        val FirmName: String,
        val GrandTotal: Int,
        val Id: Int,
        val InvoiceDate: String,
        val InvoiceNo: Int,
        val Mobile: String,
        val PanNo: Any,
        val PaymentMode: String,
        val PurchaseType: String,
        val RoundOff: Int,
        val Session: String,
        val ShippingAddress: String,
        val Status: String,
        val TotalAmount: Double,
        val TotalBV: Double,
        val TotalGST: Double,
        val TotalPV: Double,
        val UserId: String,
        val UserName: String,
        val WalletType: String
    )
}