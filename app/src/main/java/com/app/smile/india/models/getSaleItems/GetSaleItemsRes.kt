/*
 * *
 *  * Created by Prady on 8/17/24, 5:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/17/24, 5:21 PM
 *
 */

package com.app.smile.india.models.getSaleItems

data class GetSaleItemsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val BV: Double,
        val Barcode: String,
        val CGSTPer: Double,
        val DP: Double,
        val EntryBy: String,
        val EntryDate: String,
        val GSTType: String,
        val HSNCode: String,
        val IGSTPer: Double,
        val Id: Int,
        val InvoiceNo: Int,
        val MRP: Double,
        val PV: Double,
        val ProductId: Double,
        val ProductName: String,
        val ProductImage: String,
        val Quantity: Int,
        val SGSTPer: Double,
        val Session: String,
        val TotalAmount: Double,
        val TotalBV: Double,
        val TotalCGST: Double,
        val TotalDP: Double,
        val TotalGST: Double,
        val TotalIGST: Double,
        val TotalMRP: Double,
        val TotalPV: Double,
        val TotalSGST: Double,
        val TotalTaxable: Double
    )
}