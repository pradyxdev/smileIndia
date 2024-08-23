/*
 * *
 *  * Created by Prady on 8/17/24, 5:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/17/24, 5:21 PM
 *
 */

package com.app.smile.india.models.getSaleItems

data class GetSaleItemsReq(
    val apiname: String,
    val obj: Obj
) {
    data class Obj(
        val invoiceno: String,
        val session: String
    )
}