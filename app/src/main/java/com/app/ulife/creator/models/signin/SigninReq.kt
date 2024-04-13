/*
 * *
 *  * Created by Prady on 7/3/23, 6:11 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/3/23, 6:11 PM
 *
 */

package com.app.ulife.creator.models.signin

data class SigninReq(
    val ip: String,
    val password: String,
    val role: String,
    val userid: String
)