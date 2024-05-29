/*
 * *
 *  * Created by Prady on 4/18/24, 3:41 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/18/24, 3:41 PM
 *
 */

package com.app.ulife.creatoron.models.payoutManage.ldc

data class GetLdcDatesRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)