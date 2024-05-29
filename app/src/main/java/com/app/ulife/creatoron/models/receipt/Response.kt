/*
 * *
 *  * Created by Prady on 7/18/23, 2:19 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 2:19 PM
 *
 */

package com.app.ulife.creatoron.models.receipt

data class Response(
    val isAvailable: Boolean,
    val receiptNo: String,
    val url: String
)