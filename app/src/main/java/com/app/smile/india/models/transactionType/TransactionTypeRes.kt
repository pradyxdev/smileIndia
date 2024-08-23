/*
 * *
 *  * Created by Prady on 4/6/24, 6:15 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 6:15 PM
 *
 */

package com.app.smile.india.models.transactionType

data class TransactionTypeRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)