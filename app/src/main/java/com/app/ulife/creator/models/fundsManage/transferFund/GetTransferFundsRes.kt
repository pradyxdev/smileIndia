/*
 * *
 *  * Created by Prady on 4/12/24, 1:46 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 1:46 PM
 *
 */

package com.app.ulife.creator.models.fundsManage.transferFund

data class GetTransferFundsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)