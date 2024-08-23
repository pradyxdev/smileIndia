/*
 * *
 *  * Created by Prady on 4/15/24, 12:58 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 12:58 PM
 *
 */

package com.app.smile.india.models.withdrawManage.deduction

data class Data(
    val CappingAmount: Double,
    val CashWallet: Double,
    val CashWalletPercent: Double,
    val MaxDepositAmount: Double,
    val MinDepositAmount: Double,
    val admincharge: Double,
    val id: Int,
    val tdswithoutpan: Double,
    val tdswithpan: Double
)