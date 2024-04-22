/*
 * *
 *  * Created by Prady on 4/22/24, 1:04 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/22/24, 1:04 PM
 *
 */

package com.app.ulife.creator.models.paysprint.fastag.psFastagOp

data class Data(
    val `data`: List<DataX>,
    val message: String,
    val responsecode: Int,
    val status: Boolean
)