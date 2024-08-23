/*
 * *
 *  * Created by Prady on 4/12/24, 4:53 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 4:41 PM
 *
 */

package com.app.smile.india.models.epinManage.planList

data class GetPlanRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)