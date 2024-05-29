/*
 * *
 *  * Created by Prady on 4/16/24, 3:20 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 3:20 PM
 *
 */

package com.app.ulife.creatoron.models.passwordManage.accPassword

data class Obj(
    val newpassword: String,
    val oldpassword: String,
    val role: String,
    val userid: String
)