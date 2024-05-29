/*
 * *
 *  * Created by Prady on 4/5/24, 4:19 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/5/24, 4:19 PM
 *
 */

package com.app.ulife.creatoron.models.checkout

data class CheckoutOrderRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)