/*
 * *
 *  * Created by Prady on 4/12/24, 4:57 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 4:57 PM
 *
 */

package com.app.ulife.creator.models.epinManage.epinReport

data class EPinReportRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)