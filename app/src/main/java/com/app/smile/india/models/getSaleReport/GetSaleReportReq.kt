/*
 * *
 *  * Created by Prady on 8/17/24, 4:58 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/17/24, 4:58 PM
 *
 */

package com.app.smile.india.models.getSaleReport

data class GetSaleReportReq(
    val apiname: String,
    val obj: Obj
) {
    data class Obj(
        val userid: String
    )
}