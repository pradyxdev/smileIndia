/*
 * *
 *  * Created by Prady on 4/6/24, 11:20 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 11:20 AM
 *
 */

package com.app.ulife.creator.models.wallet.balance

data class GetWalletBalRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)