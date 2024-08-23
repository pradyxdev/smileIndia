/*
 * *
 *  * Created by Prady on 4/12/24, 10:47 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/12/24, 10:47 AM
 *
 */

package com.app.smile.india.models.fundsManage.addFundReport

data class Data(
    val Amount: Int,
    val ApproveDate: String,
    val ChainId: String,
    val CoinPrice: String,
    val DollerPrice: String,
    val EntryDate: String,
    val FromAccount: String,
    val Id: Int,
    val NetworkName: String,
    val Reciept: String,
    val Status: String,
    val ToAccount: String,
    val TransactionId: String,
    val TransferCoin: String,
    val Type: String,
    val UserId: String,
    val UserName: String,
    val WalletType: String
)