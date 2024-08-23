/*
 * *
 *  * Created by Prady on 4/16/24, 3:24 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/16/24, 3:24 PM
 *
 */

package com.app.smile.india.models.updateProfile

data class UpdateProfileReq(
    val apiname: String,
    val obj: Obj
)

data class Obj(
    val Aadhar: String,
    val AccHolderName: String,
    val AccNo: String,
    val BankName: String,
    val BranchName: String,
    val DateOfBirth: String,
    val Gst_No: String,
    val IFSC: String,
    val Nominee_Name: String,
    val Nominee_Relation: String,
    val PanNo: String,
    val Pincode: String,
    val Profession: String,
    val Voter_No: String,
    val address: String,
    val email: String,
    val gender: String,
    val guardian: String,
    val phone: String,
    val photo: String,
    val userid: String,
    val username: String
)
