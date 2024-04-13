/*
 * *
 *  * Created by Prady on 7/3/23, 6:12 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/3/23, 6:12 PM
 *
 */

package com.app.ulife.creator.models.signin

data class SigninRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean,
    val token: String
)