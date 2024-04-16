/*
 * *
 *  * Created by Prady on 4/13/24, 6:59 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 6:59 PM
 *
 */

package com.app.ulife.creator.models.fundsManage.transferFundReport

data class Data(
    val Amount: Int,
    val EntryDate: String,
    val FUserName: String,
    val Id: Int,
    val ToUserId: String,
    val UserId: String,
    val UserName: String,
    val UserType: String,
    val WalletType: String
)