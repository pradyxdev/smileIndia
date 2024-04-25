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
    @SerializedName("3G/4G")
    val G34: List<G4G>,
    @SerializedName("TOPUP")
    val topup: List<G4G>,
    @SerializedName("COMBO")
    val combo: List<G4G>,
    @SerializedName("RATE CUTTER")
    val rateCutter: List<G4G>,
    @SerializedName("Romaing")
    val romaing: List<G4G>,
    @SerializedName("FULLTT")
    val fullTT: List<G4G>
)