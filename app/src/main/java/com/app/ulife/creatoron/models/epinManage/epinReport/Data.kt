/*
 * *
 *  * Created by Prady on 4/12/24, 4:57 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 4:57 PM
 *
 */

package com.app.ulife.creatoron.models.epinManage.epinReport

data class Data(
    val GeneratedBy: String,
    val GeneratedDate: String,
    val Id: Int,
    val PackageId: Int,
    val PinAmount: Int,
    val PinNumber: String,
    val Status: String,
    val UsedDate: String,
    val UsedUserId: String,
    val UserId: String,
    val rowid: Int,
    val username: String
)