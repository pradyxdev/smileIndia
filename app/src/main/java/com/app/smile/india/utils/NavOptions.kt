/*
 * *
 *  * Created by Prady on 4/10/23, 10:55 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/10/23, 10:55 AM
 *
 */

package com.app.smile.india.utils


import androidx.navigation.NavOptions
import com.app.smile.india.R

fun getNavOptions(): NavOptions? {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.fade_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.fade_in)
        .setPopExitAnim(R.anim.fade_out)
        .build()
}