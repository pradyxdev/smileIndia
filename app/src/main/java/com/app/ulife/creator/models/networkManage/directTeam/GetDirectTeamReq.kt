/*
 * *
 *  * Created by Prady on 4/10/24, 5:02 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 4:25 PM
 *
 */

package com.app.ulife.creator.models.networkManage.directTeam

data class GetDirectTeamReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val from: String,
    val sp: String,
    val status: String,
    val to: String,
    val userid: String
)