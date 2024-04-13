/*
 * *
 *  * Created by Prady on 4/13/24, 1:58 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 1:58 PM
 *
 */

package com.app.ulife.creator.models.signUp.tmp

data class SignUpNewReq(
    val AdharNo: String,
    val CountryId: Int,
    val PanNo: String,
    val Pincode: String,
    val VillageName: String,
    val addresss: String,
    val cityname: String,
    val email: String,
    val gender: String,
    val mobile: String,
    val sponsorid: String,
    val standingposition: String,
    val statename: String,
    val username: String
)