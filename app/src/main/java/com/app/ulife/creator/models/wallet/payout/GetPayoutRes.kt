/*
 * *
 *  * Created by Prady on 4/6/24, 12:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:45 PM
 *
 */

package com.app.ulife.creator.models.wallet.payout

data class GetPayoutRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)