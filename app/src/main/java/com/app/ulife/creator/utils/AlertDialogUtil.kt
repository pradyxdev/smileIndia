/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.ulife.creator.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.app.ulife.creator.R

object AlertDialogUtil {
    /* This is an extension function. It is used to extend the functionality of a class. */
    fun Context.showAlertDialog(
        title: String? = null,
        message: String,
        posBtnText: String? = null,
        negBtnText: String? = null,
        showNegBtn: Boolean = true,
        callback: () -> Unit
    ) {
        /* Creating an alert dialog with the given parameters. */
        AlertDialog.Builder(this, R.style.AlertDialogTheme).also {
            it.setTitle(title ?: getString(R.string.alert))
            it.setMessage(message)
            it.setPositiveButton(posBtnText ?: getString(R.string.yes)) { _, _ ->
                callback()
            }
            if (showNegBtn) {
                it.setNegativeButton(negBtnText ?: getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
            }
        }.create().show()
    }

}