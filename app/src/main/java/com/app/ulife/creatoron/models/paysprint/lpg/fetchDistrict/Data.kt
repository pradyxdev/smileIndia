/*
 * *
 *  * Created by Prady on 5/10/24, 6:27 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 5/10/24, 6:27 PM
 *
 */

package com.app.ulife.creatoron.models.paysprint.lpg.fetchDistrict

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("STATE ID")
    val STATEID: Int,
    val VALUE: Int,
    val VALUE_DISPLAY: String
)