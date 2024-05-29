/*
 * *
 *  * Created by Prady on 4/12/24, 6:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 6:03 PM
 *
 */

package com.app.ulife.creatoron.models.epinManage.epinTransferReport

data class EpinTransferReportRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)