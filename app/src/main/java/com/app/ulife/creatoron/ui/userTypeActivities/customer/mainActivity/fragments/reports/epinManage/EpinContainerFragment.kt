/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.epinManage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.FragmentEpinContainerBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.getNavOptions
import com.app.ulife.creatoron.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class EpinContainerFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentEpinContainerBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpinContainerBinding.inflate(layoutInflater)
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
                findNavController().navigate(R.id.epinTopupFragment, args, getNavOptions())
            }

            card2.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
//                findNavController().navigate(R.id.epinGenerateFragment, args, getNavOptions())
                findNavController().navigate(R.id.epinRequestFragment, args, getNavOptions())
            }

            card22.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
//                findNavController().navigate(R.id.epinGenerateFragment, args, getNavOptions())
                findNavController().navigate(R.id.epinRequestReportFragment, args, getNavOptions())
            }

            card3.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.epinReportFragment, args, getNavOptions())
            }

            card4.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.transferEpinFragment, args, getNavOptions())
            }

            card5.setOnClickListener {
                val args = Bundle()
                args.putString("", "")
                findNavController().navigate(R.id.transferEpinReportFragment, args, getNavOptions())
            }
        }
    }
}