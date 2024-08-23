/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 10/01/23, 6:40 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.roundPay.dthPlanList.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.smile.india.adapters.DthPlanListAdapter
import com.app.smile.india.databinding.FragmentPlanListCommonBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.bbpsRecharge.dthPlan.PDetial
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.roundPay.dthPlanList.DthListActivity
import com.app.smile.india.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class PlanFragment : Fragment(), KodeinAware, DthPlanListAdapter.OnItemClickListener {
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
        if ((activity as DthListActivity).plansList.isNullOrEmpty()) {
            binding.animation.visibility = View.VISIBLE
//            binding.root.snackbar("error: no plans found !!", "i")
        } else {
            binding.apply {

                val filteredList = mutableListOf<PDetial>()
                filteredList.clear()
                for (i in (activity as DthListActivity).plansList.indices) {
                    if ((activity as DthListActivity).plansList[i].data_Id == Constants.planTypeDthPlan) {
                        filteredList.add(
                            PDetial(
                                data_Id = (activity as DthListActivity).plansList[i].data_Id,
                                pChannelCount = "" + (activity as DthListActivity).plansList[i].pChannelCount,
                                pCount = "" + (activity as DthListActivity).plansList[i].pCount,
                                pDescription = "" + (activity as DthListActivity).plansList[i].pDescription,
                                pDetials_Id = (activity as DthListActivity).plansList[i].pDetials_Id,
                                pLangauge = "" + (activity as DthListActivity).plansList[i].pLangauge,
                                packageId = "" + (activity as DthListActivity).plansList[i].packageId,
                                packageName = "" + (activity as DthListActivity).plansList[i].packageName,
                            )
                        )
                    }
                }

                rvList.adapter =
                    DthPlanListAdapter(
                        requireContext(),
                        filteredList,
                        (activity as DthListActivity).priceList,
                        this@PlanFragment
                    )
                animation.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(rs: String, desc: String, typesId: Int) {
        (activity as DthListActivity).getPlanDetails(rs, desc, typesId)
    }
}