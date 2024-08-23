/*
 * *
 *  * Created by Prady on 4/12/24, 5:14 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 5:14 PM
 *
 */

package com.app.smile.india.models.epinManage.epinTopup

data class Obj(
    val epinid: String,
    val paymenttype: String,
    val planid: String,
    val topupuserid: String,
    val userid: String
)