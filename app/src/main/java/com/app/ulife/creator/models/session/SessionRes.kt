/*
 * *
 *  * Created by Prady on 7/3/23, 5:24 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/3/23, 5:24 PM
 *
 */

package com.app.ulife.creator.models.session

data class SessionRes(
    val msg: String,
    val response: List<Response>,
    val status: Boolean
)