/*
 * *
 *  * Created by Prady on 4/25/24, 4:23 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/25/24, 4:23 PM
 *
 */

package com.app.ulife.creator.models.paysprint.dthHlr

data class Data(
    val info: List<Info>,
    val message: String,
    val response_code: Int,
    val status: Boolean
)