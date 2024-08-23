/*
 * *
 *  * Created by Prady on 5/10/24, 6:26 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 5/10/24, 6:26 PM
 *
 */

package com.app.smile.india.models.paysprint.lpg.fetchDistrict

data class GetLpgDistrictReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val StateId: String
//    , val DistrictId: String
)