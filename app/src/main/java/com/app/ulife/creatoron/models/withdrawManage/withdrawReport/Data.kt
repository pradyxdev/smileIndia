/*
 * *
 *  * Created by Prady on 4/15/24, 2:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 2:21 PM
 *
 */

package com.app.ulife.creatoron.models.withdrawManage.withdrawReport

data class Data(
    val AccHolderName: String,
    val AccNo: String,
    val AdminCharge: Double,
    val Amount: Int,
    val ApproveDate: String,
    val BankName: String,
    val ChainId: String,
    val CoinPrice: String,
    val DollerPrice: String,
    val EntryDate: String,
    val FromAccount: String,
    val IFSC: String,
    val Id: Int,
    val NetAmount: Double,
    val NetworkName: String,
    val RejectReason: String,
    val ShoppingCharge: Int,
    val Status: String,
    val Tax: Double,
    val ToAccount: String,
    val TransactionId: String,
    val TransferCoin: String,
    val UserId: String,
    val UserName: String,
    val WalletType: String
)