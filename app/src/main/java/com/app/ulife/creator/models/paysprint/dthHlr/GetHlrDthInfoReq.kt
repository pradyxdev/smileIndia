/*
 * *
 *  * Created by Prady on 4/25/24, 4:22 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/25/24, 4:22 PM
 *
 */

package com.app.ulife.creator.models.paysprint.dthHlr

data class GetHlrDthInfoReq(
    val canumber: String,
    val op: String,
    val userid: String
)