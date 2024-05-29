/*
 * *
 *  * Created by Prady on 4/3/23, 12:41 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/3/23, 12:41 PM
 *
 */

package com.app.ulife.creatoron.ui.authActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.ulife.creatoron.databinding.FragmentIntroScreenBinding
import com.app.ulife.creatoron.factories.AuthVMF
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.viewModels.AuthVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class IntroScreenFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentIntroScreenBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: AuthVMF by instance()
    private lateinit var viewModel: AuthVM

    private lateinit var preferenceManager: PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[AuthVM::class.java]
        setupViews()
        setupListeners()
    }

    private fun setupViews() {

    }

    private fun setupListeners() {
        binding.apply {
            btnSignin.setOnClickListener {
                val action =
                    IntroScreenFragmentDirections.actionIntroScreenFragmentToSigninFragment()
                findNavController().navigate(action)
            }
            btnSignup.setOnClickListener {
                val action =
                    IntroScreenFragmentDirections.actionIntroScreenFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }
    }
}