/*
 * *
 *  * Created by Prady on 7/18/23, 2:19 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 2:19 PM
 *
 */

package com.app.smile.india.models.receipt

data class GetReceiptRes(
    val msg: String,
    val response: List<Response>,
    val status: Boolean
)