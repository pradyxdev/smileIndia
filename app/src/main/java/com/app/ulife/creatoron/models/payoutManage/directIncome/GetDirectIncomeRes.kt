/*
 * *
 *  * Created by Prady on 4/15/24, 3:24 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 3:24 PM
 *
 */

package com.app.ulife.creatoron.models.payoutManage.directIncome

data class GetDirectIncomeRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)