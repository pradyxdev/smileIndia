/*
 * *
 *  * Created by Prady on 4/13/24, 6:59 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 6:59 PM
 *
 */

package com.app.smile.india.models.fundsManage.transferFundReport

internal data class GetTransReportRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)