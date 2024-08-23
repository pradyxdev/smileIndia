/*
 * *
 *  * Created by Prady on 8/17/24, 6:29 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/17/24, 6:29 PM
 *
 */

package com.app.smile.india.models.getSubCategory

data class GetSubCategoryRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val CategoryId: Int,
        val CategoryName: String,
        val Id: Int,
        val SubCategoryName: String,
        val subcat_img: String
    )
}