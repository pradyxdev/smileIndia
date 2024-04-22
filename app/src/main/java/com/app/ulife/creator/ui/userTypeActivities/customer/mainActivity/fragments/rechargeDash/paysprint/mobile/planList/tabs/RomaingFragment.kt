/*
 * *
 *  * Created by Prady on 4/20/24, 5:53 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/20/24, 5:48 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.ulife.creator.adapters.paysprint.MobPlanListAdapter
import com.app.ulife.creator.databinding.FragmentPlanListCommonBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.paysprint.psMobPlanList.G4G
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.MobPlanListActivity
import com.app.ulife.creator.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class RomaingFragment : Fragment(), KodeinAware, MobPlanListAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPlanListCommonBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    private var plansList = ArrayList<G4G>()
    var circle = ""
    var operator = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlanListCommonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
    }

    private fun setupViews() {
        if ((activity as MobPlanListActivity).plansRomaingList.isNullOrEmpty()) {
            binding.animation.visibility = View.VISIBLE
//            binding.root.snackbar("error: no plans found !!", "i")
        } else {
            binding.apply {
                rvList.adapter =
                    MobPlanListAdapter(
                        requireContext(),
                        (activity as MobPlanListActivity).plansRomaingList,
                        this@RomaingFragment
                    )
                animation.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(rs: String, desc: String) {
        (activity as MobPlanListActivity).getPlanDetails(rs, desc)
    }
}