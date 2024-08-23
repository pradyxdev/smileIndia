/*
 * *
 *  * Created by Prady on 4/12/24, 6:07 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 6:07 PM
 *
 */

package com.app.smile.india.models.epinManage.epinGenerate

import com.app.smile.india.models.epinManage.epinRequest.Obj

data class EpinGenerateReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val entryby: String,
    val noofpin: String,
    val planid: String,
    val userid: String
)