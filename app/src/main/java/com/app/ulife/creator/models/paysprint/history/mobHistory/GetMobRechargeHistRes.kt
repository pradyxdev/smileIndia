/*
 * *
 *  * Created by Prady on 4/23/24, 4:48 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 4:48 PM
 *
 */

package com.app.ulife.creator.models.paysprint.history.mobHistory

data class GetMobRechargeHistRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)