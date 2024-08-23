/*
 * *
 *  * Created by Prady on 7/3/23, 7:12 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/3/23, 7:12 PM
 *
 */

package com.app.smile.india.models.events

data class EventsRes(
    val msg: String,
    val response: List<Response>,
    val status: Boolean
)