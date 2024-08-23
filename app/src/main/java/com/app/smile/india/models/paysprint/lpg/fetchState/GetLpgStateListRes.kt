/*
 * *
 *  * Created by Prady on 5/10/24, 6:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 5/10/24, 6:21 PM
 *
 */

package com.app.smile.india.models.paysprint.lpg.fetchState

data class GetLpgStateListRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)