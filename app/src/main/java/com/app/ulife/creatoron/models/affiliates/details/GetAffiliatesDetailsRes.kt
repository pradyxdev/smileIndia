/*
 * *
 *  * Created by Prady on 6/22/24, 6:01 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 6/22/24, 6:01 PM
 *
 */

package com.app.ulife.creatoron.models.affiliates.details

data class GetAffiliatesDetailsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val AffliateName: String,
        val Url: String,
        val UserId: String,
        val aff_sub1: String,
        val aff_sub2: Any,
        val aff_sub3: Any,
        val aff_sub4: Any,
        val id: Int,
        val sub_aff_id: String
    )
}