/*
 * *
 *  * Created by Prady on 7/3/23, 6:11 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/3/23, 6:11 PM
 *
 */

package com.app.prady.appbase.models.signin

data class SigninReq(
    val password: String,
    val session: String,
    val userID: String
)