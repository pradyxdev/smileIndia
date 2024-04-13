/*
 * *
 *  * Created by Prady on 4/6/24, 11:21 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 11:21 AM
 *
 */

package com.app.ulife.creator.models.wallet.history

data class GetWalletHistoryRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)