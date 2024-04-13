/*
 * *
 *  * Created by Prady on 4/10/24, 5:24 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:24 PM
 *
 */

package com.app.ulife.creator.models.networkManage.levelWise

data class GetLevelWiseRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)