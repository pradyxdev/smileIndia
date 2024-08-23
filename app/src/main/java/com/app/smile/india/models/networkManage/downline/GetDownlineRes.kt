/*
 * *
 *  * Created by Prady on 4/10/24, 5:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.smile.india.models.networkManage.downline

data class GetDownlineRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)