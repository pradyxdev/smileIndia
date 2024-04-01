/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creator.utils

import android.view.View

/*
As Android Developer, we must be familiar with setOnClickListener that listen all view click event,
but did you know that there are many hidden bug that lead to unexpected app behaviour when we don’t
handle this click event properly.

For example : When user need click action to create new order, user can simply doing rapid click to
order button that will impact calling order use case twice, it will make our app create double order.

With this extension, we can simply avoid this kind of scenario or another unexpected scenario because
of double click. This extension will give time to our view to not response another click input from user.
 */
class ThrottledClick {
    fun View.onThrottledClick(
        throttleDelay: Long = 500L,
        onClick: (View) -> Unit
    ) {
        setOnClickListener {
            onClick(this)
            isClickable = false
            postDelayed({ isClickable = true }, throttleDelay)
        }
    }

    /*
    Usage
    /*Calling onThrottledClick with yourcustom debounce time */
    btn_order.onThrottledClick(2000L) {
         // Doing order here
       }
    */
}