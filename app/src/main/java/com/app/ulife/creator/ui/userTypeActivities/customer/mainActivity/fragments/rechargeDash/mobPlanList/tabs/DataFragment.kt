/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 10/01/23, 6:40 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.ulife.creator.adapters.PlanListAdapter
import com.app.ulife.creator.databinding.FragmentPlanListCommonBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.bbpsRecharge.mobPlan.PDetail
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.mobPlanList.PlanListActivity
import com.app.ulife.creator.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class DataFragment : Fragment(), KodeinAware, PlanListAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPlanListCommonBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
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
        if ((activity as PlanListActivity).plansList.isNullOrEmpty()) {
            binding.animation.visibility = View.VISIBLE
//            binding.root.snackbar("error: no plans found !!", "i")
        } else {
            binding.apply {
                val filteredList = mutableListOf<PDetail>()
                filteredList.clear()
                for (i in (activity as PlanListActivity).plansList.indices) {
                    if ((activity as PlanListActivity).plansList[i].types_Id == Constants.planTypeData) {
                        filteredList.add(
                            PDetail(
                                desc = "" + (activity as PlanListActivity).plansList[i].desc,
                                rs = "" + (activity as PlanListActivity).plansList[i].rs,
                                types_Id = (activity as PlanListActivity).plansList[i].types_Id,
                                validity = "" + (activity as PlanListActivity).plansList[i].validity,
                            )
                        )
                    }
                }

                rvList.adapter =
                    PlanListAdapter(
                        requireContext(),
                        filteredList,
                        this@DataFragment
                    )
                animation.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(rs: String, desc: String, typesId: Int) {
        (activity as PlanListActivity).getPlanDetails(rs, desc, typesId)
    }
}