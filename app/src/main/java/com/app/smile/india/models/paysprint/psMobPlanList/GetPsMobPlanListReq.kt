/*
 * *
 *  * Created by Prady on 4/20/24, 5:20 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/20/24, 5:20 PM
 *
 */

package com.app.smile.india.models.paysprint.psMobPlanList

data class GetPsMobPlanListReq(
    val circle: String,
    val op: String,
    val userid: String
)