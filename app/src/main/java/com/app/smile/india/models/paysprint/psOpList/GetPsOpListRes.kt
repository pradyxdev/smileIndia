/*
 * *
 *  * Created by Prady on 4/20/24, 3:39 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/20/24, 3:39 PM
 *
 */

package com.app.smile.india.models.paysprint.psOpList

data class GetPsOpListRes(
    val `data`: Data,
    val message: String,
    val requestId: String,
    val status: Boolean
)