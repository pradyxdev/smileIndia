/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.reports.fundsManage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.app.smile.india.R
import com.app.smile.india.databinding.FragmentFundsContainerBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.MainActivity
import com.app.smile.india.utils.getNavOptions
import com.app.smile.india.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class FundsContainerFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentFundsContainerBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFundsContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(isVisible = false)
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }

            card1.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.addFundFragment, args, getNavOptions())
            }

            card2.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.addFundsReportFragment, args, getNavOptions())
            }

            card3.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.transferFundFragment, args, getNavOptions())
            }

            card4.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.transferFundReportFragment, args, getNavOptions())
            }
        }
    }
}