/*
 * *
 *  * Created by Prady on 7/3/23, 7:17 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/3/23, 7:17 PM
 *
 */

package com.app.smile.india.models.userDetails

data class UserDetailsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)