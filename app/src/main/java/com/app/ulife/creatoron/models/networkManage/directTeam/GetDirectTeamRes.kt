/*
 * *
 *  * Created by Prady on 4/10/24, 5:02 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 4:30 PM
 *
 */

package com.app.ulife.creatoron.models.networkManage.directTeam

data class GetDirectTeamRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)