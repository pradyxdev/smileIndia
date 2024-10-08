/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.smile.india.utils

import android.content.Context
import com.app.smile.india.helpers.Constants
import com.google.firebase.database.FirebaseDatabase

fun Context.getAppVersioning() = FirebaseDatabase.getInstance(Constants.FirebaseDBUrl)
    .getReference("AppName/smileIndia/Versioning")

fun Context.getUserDetails() =
    FirebaseDatabase.getInstance(Constants.FirebaseDBUrl)
        .getReference("AppName/smileIndia/Users")
