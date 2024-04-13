/*
 * *
 *  * Created by Prady on 4/9/24, 6:36 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/9/24, 6:36 PM
 *
 */

package com.app.ulife.creator.models.bbpsRecharge.mobPlan

data class Data(
    val `data`: List<DataX>,
    val pDetails: List<PDetail>,
    val rootNode: List<RootNode>,
    val types: List<Type>
)