/*
 * *
 *  * Created by Prady on 4/16/24, 2:29 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 2:29 PM
 *
 */

package com.app.smile.india.models.passwordManage.changePassword

data class ChangePasswordReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val newpassword: String,
    val oldpassword: String,
    val userid: String
)