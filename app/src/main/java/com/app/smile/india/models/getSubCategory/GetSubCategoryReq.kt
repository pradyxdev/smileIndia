/*
 * *
 *  * Created by Prady on 8/17/24, 6:29 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/17/24, 6:29 PM
 *
 */

package com.app.smile.india.models.getSubCategory

data class GetSubCategoryReq(
    val apiname: String,
    val obj: Obj
) {
    data class Obj(
        val categoryid: String
    )
}