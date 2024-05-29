/*
 * *
 *  * Created by Prady on 4/15/24, 10:57 AM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/15/24, 10:57 AM
 *
 */

package com.app.ulife.creatoron.models.bbpsRecharge.operatorLocal

data class GetOperatorLocalRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)