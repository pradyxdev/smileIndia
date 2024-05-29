/*
 * *
 *  * Created by Prady on 4/15/24, 12:58 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 12:58 PM
 *
 */

package com.app.ulife.creatoron.models.withdrawManage.deduction

data class GetDeductionRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)