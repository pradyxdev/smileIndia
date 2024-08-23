/*
 * *
 *  * Created by Prady on 4/13/24, 12:48 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 12:48 PM
 *
 */

package com.app.smile.india.models.fundsManage.add

data class AddFundsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)