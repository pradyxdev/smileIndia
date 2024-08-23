/*
 * *
 *  * Created by Prady on 4/15/24, 3:39 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 3:39 PM
 *
 */

package com.app.smile.india.models.payoutManage.matchingIncome

data class GetMatchingIncomeRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)