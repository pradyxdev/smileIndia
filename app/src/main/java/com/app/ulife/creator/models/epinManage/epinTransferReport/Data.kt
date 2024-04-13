/*
 * *
 *  * Created by Prady on 4/12/24, 6:03 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 6:03 PM
 *
 */

package com.app.ulife.creator.models.epinManage.epinTransferReport

data class Data(
    val Amount: Int,
    val AvailablePin: Int,
    val EntryBy: String,
    val EntryDate: String,
    val Id: String,
    val NoOfPin: Int,
    val TotalAmount: Int,
    val UserId: String,
    val rowid: Int,
    val username: String
)