/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 10/01/23, 6:40 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.dthPlanList.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.ulife.creator.adapters.DthPlanListAdapter
import com.app.ulife.creator.databinding.FragmentPlanListCommonBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.bbpsRecharge.dthPlan.PDetial
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.rechargeDash.dthPlanList.DthListActivity
import com.app.ulife.creator.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class AddOnFragment : Fragment(), KodeinAware, DthPlanListAdapter.OnItemClickListener {
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
                    if ((activity as DthListActivity).plansList[i].data_Id == Constants.planTypeDthAddOn) {
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
                        this@AddOnFragment
                    )
                animation.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(rs: String, desc: String, typesId: Int) {
        (activity as DthListActivity).getPlanDetails(rs, desc, typesId)
    }
}