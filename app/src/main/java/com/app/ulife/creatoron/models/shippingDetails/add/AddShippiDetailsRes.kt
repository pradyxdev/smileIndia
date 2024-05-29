/*
 * *
 *  * Created by Prady on 4/5/24, 2:23 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/5/24, 2:23 PM
 *
 */

package com.app.ulife.creatoron.models.shippingDetails.add

data class AddShippiDetailsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)