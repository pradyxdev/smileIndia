/*
 * *
 *  * Created by Prady on 4/10/24, 12:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 12:03 PM
 *
 */

package com.app.ulife.creator.models.bbpsRecharge.dthPlan

data class Data(
    val `data`: List<DataX>,
    val pDetials: List<PDetial>,
    val price: List<Price>,
    val rootNode: List<RootNode>
)