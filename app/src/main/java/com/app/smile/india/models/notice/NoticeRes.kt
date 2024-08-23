/*
 * *
 *  * Created by Prady on 7/3/23, 6:50 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/3/23, 6:50 PM
 *
 */

package com.app.smile.india.models.notice

data class NoticeRes(
    val msg: String,
    val response: List<Response>,
    val status: Boolean
)