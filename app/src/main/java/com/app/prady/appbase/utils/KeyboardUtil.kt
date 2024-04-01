/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.prady.appbase.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {

    // Show keyboard for EditText
    fun showKeyboard(view: View) {
        view.requestFocus()
        view.post {
            val inputMethod =
                view.context.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
            inputMethod.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    // Hide keyboard for EditText
    fun hideKeyboard(view: View) {
        val inputMethod =
            view.context.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(view.windowToken, 0)
    }
}