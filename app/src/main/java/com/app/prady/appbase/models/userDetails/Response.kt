/*
 * *
 *  * Created by Prady on 7/3/23, 7:17 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/3/23, 7:17 PM
 *
 */

package com.app.prady.appbase.models.userDetails

data class Response(
    val address: String,
    val courseName: String,
    val dob: String,
    val enrollmentNo: String,
    val fatherName: String,
    val fullName: String,
    val mobileNo: String,
    val motherName: String,
    val subjectName: String
)