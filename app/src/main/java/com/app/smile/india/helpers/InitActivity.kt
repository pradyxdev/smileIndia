/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */

package com.app.smile.india.helpers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.smile.india.R
import com.google.android.material.appbar.MaterialToolbar
import java.lang.reflect.ParameterizedType

abstract class InitActivity<VM : ViewModel, V_BINDING : ViewDataBinding> : AppCompatActivity() {

    private var _viewModel: VM? = null
    private var _binding: V_BINDING? = null
    protected val viewModel: VM get() = _viewModel!!
    protected val binding: V_BINDING get() = _binding!!
    private var _toolbar: MaterialToolbar? = null
    val toolbar: MaterialToolbar? get() = _toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments

        _viewModel = ViewModelProvider(this)[type[0] as Class<VM>]
        _binding = DataBindingUtil.setContentView(this, layoutID())
        binding.lifecycleOwner = this
        _toolbar = binding.root.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _viewModel = null
    }

    abstract fun layoutID(): Int
}