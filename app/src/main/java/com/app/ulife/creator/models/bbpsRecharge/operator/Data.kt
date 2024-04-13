/*
 * *
 *  * Created by Prady on 4/9/24, 7:07 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/9/24, 6:35 PM
 *
 */

package com.app.ulife.creator.models.bbpsRecharge.operator

data class Data(
    val Amount: Any,
    val Instruction: Any,
    val IsActive: Boolean,
    val OperatorCode: Int,
    val Service: String,
    val PlanSPKey: String,
    val Sno: Int,
    val State: Any,
    val Type: String
)