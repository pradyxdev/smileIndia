/*
 * *
 *  * Created by Prady on 4/22/24, 11:38 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/22/24, 11:38 AM
 *
 */

package com.app.ulife.creator.models.paysprint.bbpsOpList

data class Data(
    val `data`: List<DataX>,
    val message: String,
    val responsecode: Int,
    val status: Boolean
)