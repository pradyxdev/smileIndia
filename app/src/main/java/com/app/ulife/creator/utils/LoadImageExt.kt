/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creator.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.app.ulife.creator.R
import com.bumptech.glide.Glide

class LoadImageExt {
    fun ImageView.loadImage(
        url: String,
        @DrawableRes placeholder: Int = R.drawable.ic_image_error
    ) {
        Glide.with(this)
            .load(url)
            .placeholder(placeholder)
            .into(this)
    }

    /* Usage:  Load image with extension
    img_profile_picture.loadImage(url)

    img_profile_picture.loadImage(url, R.drawable.custom_placeholder)
    */
}