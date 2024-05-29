/*
 * *
 *  * Created by Prady on 5/10/24, 6:27 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 5/10/24, 6:27 PM
 *
 */

package com.app.ulife.creatoron.models.paysprint.lpg.fetchDistrict

data class GetLpgDistrictRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)