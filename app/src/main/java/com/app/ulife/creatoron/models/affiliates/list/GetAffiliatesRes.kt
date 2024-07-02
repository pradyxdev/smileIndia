/*
 * *
 *  * Created by Prady on 6/22/24, 6:00 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 6/22/24, 5:23 PM
 *
 */

package com.app.ulife.creatoron.models.affiliates.list

data class GetAffiliatesRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val Id: Int,
        val Logo: String,
        val Name: String
    )
}