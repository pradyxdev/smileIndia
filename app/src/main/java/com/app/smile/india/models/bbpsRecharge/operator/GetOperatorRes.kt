/*
 * *
 *  * Created by Prady on 4/9/24, 6:35 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 3:38 PM
 *
 */

package com.app.smile.india.models.bbpsRecharge.operator

data class GetOperatorRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)