/*
 * *
 *  * Created by Prady on 4/15/24, 4:00 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 4:00 PM
 *
 */

package com.app.ulife.creatoron.models.payoutManage.shoppingIncome

data class GetShoppingIncomeRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)