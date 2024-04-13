/*
 * *
 *  * Created by Prady on 4/10/24, 3:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 3:03 PM
 *
 */

package com.app.ulife.creator.models.bbpsRecharge.mobileLookup

data class RootNode(
    val circle: String,
    val circleCode: String,
    val deliveryStatus: String,
    val isCircleOnly: String,
    val lookup_number: String,
    val msg: String,
    val `operator`: String,
    val spkey: String,
    val statusCode: String
)