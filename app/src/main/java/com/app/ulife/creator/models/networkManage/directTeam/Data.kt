/*
 * *
 *  * Created by Prady on 4/10/24, 4:29 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 4:29 PM
 *
 */

package com.app.ulife.creator.models.networkManage.directTeam

data class Data(
    val ActivationDate: String,
    val Direction: String,
    val Email: String,
    val Id: Int,
    val Mobile: String,
    val Photo: String,
    val RegDate: String,
    val Status: Int,
    val TotalActiveDirect: Int,
    val TotalActiveTeam: Int,
    val TotalDeactiveDirect: Int,
    val TotalDeactiveTeam: Int,
    val UserId: String,
    val UserName: String
)