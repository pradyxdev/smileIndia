/*
 * *
 *  * Created by Prady on 4/18/24, 4:22 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/18/24, 4:22 PM
 *
 */

package com.app.smile.india.models.payoutManage.ldc.report

data class GetLdcReportRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)