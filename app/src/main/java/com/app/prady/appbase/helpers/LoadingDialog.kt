/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.prady.appbase.helpers

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.app.prady.appbase.R
import com.app.prady.appbase.databinding.ViewLoadingAnimationBinding

class LoadingDialog(context: Context) : Dialog(context) {

    private lateinit var binding: ViewLoadingAnimationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewLoadingAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.color.transparent_gray)
    }
}