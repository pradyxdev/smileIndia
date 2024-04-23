/*
 * *
 *  * Created by Prady on 4/23/24, 4:48 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/23/24, 4:48 PM
 *
 */

package com.app.ulife.creator.models.paysprint.history.mobHistory

data class Data(
    val Amount: Int,
    val CaNumber: String,
    val Circle: String,
    val EntryDate: String,
    val Id: Int,
    val Message: Any,
    val OperatorName: String,
    val PlanDescription: String,
    val ReferenceId: String,
    val ResponseCode: Int,
    val Status: Int
)