/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creatoron.utils

import android.view.View

inline fun View.onDebouncedListener(
    delayInClick: Long = 5000L,
    crossinline listener: (View) -> Unit
) {
    val enableAgain = Runnable { isEnabled = true }

    setOnClickListener {
        if (isEnabled) {
            isEnabled = false
            postDelayed(enableAgain, delayInClick)
            listener(it)
        }
    }
}