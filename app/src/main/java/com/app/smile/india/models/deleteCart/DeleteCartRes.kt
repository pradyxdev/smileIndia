/*
 * *
 *  * Created by Prady on 4/4/24, 4:32 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 4:32 PM
 *
 */

package com.app.smile.india.models.deleteCart

data class DeleteCartRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)