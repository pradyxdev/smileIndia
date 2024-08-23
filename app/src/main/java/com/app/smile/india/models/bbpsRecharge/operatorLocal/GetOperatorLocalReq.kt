/*
 * *
 *  * Created by Prady on 4/15/24, 10:52 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 10:52 AM
 *
 */

package com.app.smile.india.models.bbpsRecharge.operatorLocal

data class GetOperatorLocalReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val Type: String,
    val name: String
)