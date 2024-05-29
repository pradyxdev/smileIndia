/*
 * *
 *  * Created by Prady on 4/12/24, 5:20 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 5:20 PM
 *
 */

package com.app.ulife.creatoron.models.epinManage.epinTopup

data class EpinTopupRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)