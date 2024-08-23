/*
 * *
 *  * Created by Prady on 4/16/24, 3:10 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 3:10 PM
 *
 */

package com.app.smile.india.models.passwordManage.changePassword

data class ChangePasswordRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)