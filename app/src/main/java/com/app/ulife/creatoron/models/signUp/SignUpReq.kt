/*
 * *
 *  * Created by Prady on 4/3/24, 6:34 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/3/24, 6:34 PM
 *
 */

package com.app.ulife.creatoron.models.signUp

data class SignUpReq(
    val CountryId: String,
    val Password: String,
    val addresss: String,
    val email: String,
    val gender: String,
    val mobile: String,
    val parentuserid: String,
    val regtype: String,
    val sponsorid: String,
    val standingposition: String,
    val username: String
)