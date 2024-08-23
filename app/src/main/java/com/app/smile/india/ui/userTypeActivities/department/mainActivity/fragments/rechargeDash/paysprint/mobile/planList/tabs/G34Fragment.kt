/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 10/01/23, 6:40 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.smile.india.adapters.paysprint.MobPlanListAdapter
import com.app.smile.india.databinding.FragmentPlanListCommonBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.paysprint.psMobPlanList.G4G
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.rechargeDash.paysprint.mobile.planList.MobPlanListActivity
import com.app.smile.india.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class G34Fragment : Fragment(), KodeinAware, MobPlanListAdapter.OnItemClickListener {
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
        Log.e("plansG34List ", "" + (activity as MobPlanListActivity).plansG34List)
        if ((activity as MobPlanListActivity).plansG34List.isNullOrEmpty()) {
            binding.animation.visibility = View.VISIBLE
//            binding.root.snackbar("error: no plans found !!", "i")
        } else {
            binding.apply {
                rvList.adapter =
                    MobPlanListAdapter(
                        requireContext(),
                        (activity as MobPlanListActivity).plansG34List,
                        this@G34Fragment
                    )
                animation.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(rs: String, desc: String) {
        (activity as MobPlanListActivity).getPlanDetails(rs, desc)
    }
}