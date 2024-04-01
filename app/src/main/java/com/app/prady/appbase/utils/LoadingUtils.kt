/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.prady.appbase.utils

import android.content.Context
import com.app.prady.appbase.helpers.LoadingDialog

open class LoadingUtils {
    companion object {
        private var loader: LoadingDialog? = null
        fun showDialog(
            context: Context?,
            isCancelable: Boolean
        ) {
            hideDialog()
            if (context != null) {
                try {
                    loader = LoadingDialog(context)
                    loader?.let { loadingDialog ->
                        loadingDialog.setCanceledOnTouchOutside(true)
                        loadingDialog.setCancelable(isCancelable)
                        loadingDialog.show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun hideDialog() {
            if (loader != null && loader?.isShowing!!) {
                loader = try {
                    loader?.dismiss()
                    null
                } catch (e: Exception) {
                    null
                }
            }
        }

    }
}