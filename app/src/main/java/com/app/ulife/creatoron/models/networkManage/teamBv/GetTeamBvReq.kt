/*
 * *
 *  * Created by Prady on 4/10/24, 5:59 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:59 PM
 *
 */

package com.app.ulife.creatoron.models.networkManage.teamBv

data class GetTeamBvReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val fromdate: String,
    val fromuserid: String,
    val status: String,
    val todate: String,
    val userid: String
)