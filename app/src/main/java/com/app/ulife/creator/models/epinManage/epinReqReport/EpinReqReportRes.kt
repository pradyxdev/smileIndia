/*
 * *
 *  * Created by Prady on 4/15/24, 7:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 7:21 PM
 *
 */

package com.app.ulife.creator.models.epinManage.epinReqReport

data class EpinReqReportRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)