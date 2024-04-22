/*
 * *
 *  * Created by Prady on 4/20/24, 5:49 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/20/24, 5:49 PM
 *
 */

package com.app.ulife.creator.models.paysprint.psMobPlanList

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("2G")
    val G2: List<G4G>,
    @SerializedName("3G/4G")
    val G23: List<G4G>,
    val COMBO: List<G4G>,
    val FULLTT: List<G4G>,
    val Romaing: List<G4G>,
    val SMS: List<G4G>,
    val TOPUP: List<G4G>
)