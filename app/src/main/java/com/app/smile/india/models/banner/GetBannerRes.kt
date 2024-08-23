/*
 * *
 *  * Created by Prady on 4/4/24, 10:49 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 10:49 AM
 *
 */

package com.app.smile.india.models.banner

data class GetBannerRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)