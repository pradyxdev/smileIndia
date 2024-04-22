/*
 * *
 *  * Created by Prady on 4/20/24, 3:48 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/20/24, 3:48 PM
 *
 */

package com.app.ulife.creator.models.paysprint.hlrCheck

data class HlrCheckRes(
    val `data`: Data,
    val message: String,
    val requestId: String,
    val status: Boolean
)