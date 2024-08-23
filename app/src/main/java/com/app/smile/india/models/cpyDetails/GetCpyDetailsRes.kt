/*
 * *
 *  * Created by Prady on 4/10/24, 7:04 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 7:04 PM
 *
 */

package com.app.smile.india.models.cpyDetails

data class GetCpyDetailsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)