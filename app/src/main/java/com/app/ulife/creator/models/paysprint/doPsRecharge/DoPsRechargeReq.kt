/*
 * *
 *  * Created by Prady on 4/20/24, 3:47 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/20/24, 3:47 PM
 *
 */

package com.app.ulife.creator.models.paysprint.doPsRecharge

data class DoPsRechargeReq(
    val amount: String,
    val canumber: String,
    val operator: String,
    val userid: String,
    val OperatorName: String,
    val Circle: String,
    val PlanDescription: String
)