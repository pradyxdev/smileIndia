/*
 * *
 *  * Created by Prady on 7/18/23, 2:19 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 2:19 PM
 *
 */

package com.app.prady.appbase.models.receipt

data class GetReceiptReq(
    val enrollId: String,
    val sess: String,
    val type: String
)