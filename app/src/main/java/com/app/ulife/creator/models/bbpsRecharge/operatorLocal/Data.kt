/*
 * *
 *  * Created by Prady on 4/15/24, 10:57 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 10:57 AM
 *
 */

package com.app.ulife.creator.models.bbpsRecharge.operatorLocal

data class Data(
    val Amount: Any,
    val Instruction: Any,
    val IsActive: Boolean,
    val OperatorCode: Int,
    val PlanSPKey: String,
    val Service: String,
    val Sno: Int,
    val State: Any,
    val Type: String
)