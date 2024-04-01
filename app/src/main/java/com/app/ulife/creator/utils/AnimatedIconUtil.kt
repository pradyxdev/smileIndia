/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creator.utils

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView

class AnimatedIconUtil {
    // Animated icons
    fun startIcon(imageView: ImageView?) {
        if (imageView == null) {
            return
        }
        startIcon(imageView.drawable)
    }

    fun startIcon(drawable: Drawable?) {
        if (drawable == null) {
            return
        }
        try {
            (drawable as Animatable).start()
        } catch (e: ClassCastException) {
            Log.e(TAG, "icon animation requires AnimVectorDrawable")
        }
    }

    fun resetAnimatedIcon(imageView: ImageView?) {
        if (imageView == null) {
            return
        }
        try {
            val animatable = imageView.drawable as Animatable
            animatable.stop()
            imageView.setImageDrawable(null)
            imageView.setImageDrawable(animatable as Drawable)
        } catch (e: ClassCastException) {
            Log.e(TAG, "resetting animated icon requires AnimVectorDrawable")
        }
    }
}