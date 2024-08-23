/*
 * *
 *  * Created by Prady on 4/13/24, 1:58 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/13/24, 1:58 PM
 *
 */

package com.app.smile.india.models.signUp.tmp

data class SignUpNewReq(
    val RegiType: String,
    val FatherName: Any?=null,
    val bankname: Any?=null,
    val BranchName: Any?=null,
    val accno: Any?=null,
    val ifsc: Any?=null,
    val accholname: Any?=null,
    val AdharNo: String,
    val CountryId: Int,
    val PanNo: String,
    val Pincode: String,
    val VillageName: String,
    val addresss: String,
    val cityname: String,
    val email: String,
//    val gender: String,
    val mobile: String,
    val sponsorid: String,
    val standingposition: Any?=null,
    val statename: String,
    val username: String
)