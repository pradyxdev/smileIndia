/*
 * *
 *  * Created by Prady on 4/10/24, 6:01 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 6:01 PM
 *
 */

package com.app.ulife.creator.models.networkManage.teamBv

data class GetTeamBvRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)