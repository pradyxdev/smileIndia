/*
 * *
 *  * Created by Prady on 4/23/24, 4:53 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 4:53 PM
 *
 */

package com.app.ulife.creatoron.models.paysprint.history.mobHistory

data class GetMobRechargeHistReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val from: String,
    val to: String,
    val userid: String
)