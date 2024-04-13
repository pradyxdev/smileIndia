/*
 * *
 *  * Created by Prady on 4/3/24, 6:12 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/3/24, 6:12 PM
 *
 */

package com.app.ulife.creator.models.userDetails

data class Data(
    val Aadhar: String,
    val AccHolderName: String,
    val AccNo: String,
    val ActivationDate: String,
    val Address: String,
    val BankName: String,
    val BranchName: String,
    val ChainId: Any,
    val CityName: Any,
    val DateOfBirth: Any,
    val Email: String,
    val FromAccount: Any,
    val Gender: String,
    val Gst_No: Any,
    val IFSC: String,
    val Id: Int,
    val IsBlock: Int,
    val JoiningAmount: String,
    val Mobile: String,
    val NetworkName: String,
    val Nominee_Name: Any,
    val Nominee_Relation: Any,
    val PanNo: String,
    val ParentName: Any,
    val ParentUserId: String,
    val Password: String,
    val Photo: String,
    val Pincode: String,
    val Profession: Any,
    val RegDate: String,
    val SponsorId: String,
    val SponsorName: Any,
    val StandingPosition: Int,
    val StateName: Any,
    val Status: Int,
    val ToAccount: Any,
    val TransactionId: String,
    val UserId: String,
    val UserName: String,
    val VillageName: Any,
    val Voter_No: Any,
    val guardian: String
)