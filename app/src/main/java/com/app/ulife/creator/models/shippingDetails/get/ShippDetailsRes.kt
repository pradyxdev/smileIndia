/*
 * *
 *  * Created by Prady on 4/4/24, 5:40 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 5:40 PM
 *
 */

package com.app.ulife.creator.models.shippingDetails.get

data class ShippDetailsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)