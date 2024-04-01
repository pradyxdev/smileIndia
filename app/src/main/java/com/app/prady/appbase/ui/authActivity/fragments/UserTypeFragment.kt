/*
 * *
 *  * Created by Prady on 4/3/23, 12:41 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/3/23, 12:41 PM
 *
 */

package com.app.prady.appbase.ui.authActivity.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.prady.appbase.databinding.FragmentUserTypeBinding
import com.app.prady.appbase.factories.AuthVMF
import com.app.prady.appbase.helpers.Constants
import com.app.prady.appbase.helpers.PreferenceManager
import com.app.prady.appbase.utils.toast
import com.app.prady.appbase.viewModels.AuthVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class UserTypeFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentUserTypeBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: AuthVMF by instance()
    private lateinit var viewModel: AuthVM

    private lateinit var preferenceManager: PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserTypeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[AuthVM::class.java]
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        viewModel.userType.observe(requireActivity()) {
            when {
                it.isNullOrEmpty() -> Log.e("vmUser ", "Type not selected !")
                it.equals(Constants.userTypeCustomer) -> {
                    binding.card1.isChecked = true
                    binding.card2.isChecked = false
                    binding.card3.isChecked = false
                }
                it.equals(Constants.userTypeDepartment) -> {
                    binding.card1.isChecked = false
                    binding.card2.isChecked = true
                    binding.card3.isChecked = false
                }
                it.equals(Constants.userTypeAdmin) -> {
                    binding.card1.isChecked = false
                    binding.card2.isChecked = false
                    binding.card3.isChecked = true
                }
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            card1.setOnClickListener {
                card1.isChecked = true
                card2.isChecked = false
                card3.isChecked = false
                viewModel.setUserType(Constants.userTypeCustomer)
            }
            card2.setOnClickListener {
                card1.isChecked = false
                card2.isChecked = true
                card3.isChecked = false
                viewModel.setUserType(Constants.userTypeDepartment)
            }
            card3.setOnClickListener {
                card1.isChecked = false
                card2.isChecked = false
                card3.isChecked = true
                viewModel.setUserType(Constants.userTypeAdmin)
            }
            btnSubmit.setOnClickListener {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        if (!viewModel.userType.value.isNullOrEmpty()) {
            val action = UserTypeFragmentDirections.actionUserTypeFragmentToSigninFragment()
            findNavController().navigate(action)
        } else {
            context?.toast("Please select a user type first !")
        }
    }
}