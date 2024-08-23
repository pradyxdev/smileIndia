/*
 * *
 *  * Created by Prady on 4/10/23, 11:04 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/10/23, 11:04 AM
 *
 */

package com.app.smile.india.models.social

import com.google.gson.annotations.SerializedName

data class SocialResItem(
    @SerializedName("Date of posting")
    val dop: String,
    val Hashtag: String,
    @SerializedName("Like count")
    val likeCount: Int,
    @SerializedName("Retweet count")
    val retweetCount: Int,
    val Text: String,
    @SerializedName("User handle")
    val userHandle: String,
    val Username: String
)