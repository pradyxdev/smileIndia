/*
 * *
 *  * Created by Prady on 5/10/24, 6:44 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 5/10/24, 6:44 PM
 *
 */

package com.app.ulife.creatoron.models.paysprint.lpg.fetchDistributer

data class GetLpgDistributerRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)