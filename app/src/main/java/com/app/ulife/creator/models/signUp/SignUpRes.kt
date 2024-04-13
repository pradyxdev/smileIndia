/*
 * *
 *  * Created by Prady on 4/3/24, 6:36 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/3/24, 6:36 PM
 *
 */

package com.app.ulife.creator.models.signUp

data class SignUpRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)