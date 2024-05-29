/*
 * *
 *  * Created by Prady on 4/10/24, 5:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.ulife.creatoron.models.networkManage.downline

data class Data(
    val ActivationDate: String,
    val Direction: String,
    val Email: String,
    val Id: Int,
    val Mobile: String,
    val Photo: Any,
    val RegDate: String,
    val SponsorId: String,
    val SponsorName: String,
    val Status: Int,
    val TopupAmount: Int,
    val TotalActiveDirect: Int,
    val TotalActiveTeam: Int,
    val TotalDeactiveDirect: Int,
    val TotalDeactiveTeam: Int,
    val UserId: String,
    val UserName: String
)