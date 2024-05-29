/*
 * *
 *  * Created by Prady on 4/12/24, 6:25 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 6:25 PM
 *
 */

package com.app.ulife.creatoron.models.epinManage.epinTransfer

data class EpinTransferReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val entryby: String,
    val noofpin: String,
    val planid: String,
    val userid: String
)