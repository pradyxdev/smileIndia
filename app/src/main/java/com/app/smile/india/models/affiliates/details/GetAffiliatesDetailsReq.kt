/*
 * *
 *  * Created by Prady on 6/22/24, 6:01 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 6/22/24, 6:01 PM
 *
 */

package com.app.smile.india.models.affiliates.details

data class GetAffiliatesDetailsReq(
    val apiname: String,
    val obj: Obj
) {
    data class Obj(
        val type: String,
        val userid: String
    )
}