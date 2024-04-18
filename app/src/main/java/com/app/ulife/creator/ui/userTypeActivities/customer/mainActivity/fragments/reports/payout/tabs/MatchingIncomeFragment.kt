/*
 * *
 *  * Created by Prady on 8/3/23, 11:19 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 8/3/23, 11:14 AM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.reports.payout.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ulife.creator.R
import com.app.ulife.creator.adapters.payout.MatchingIncomeAdapter
import com.app.ulife.creator.databinding.FragmentSeeAllBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.payoutManage.directIncome.GetDirectIncomeReq
import com.app.ulife.creator.models.payoutManage.matchingIncome.GetMatchingIncomeRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class MatchingIncomeFragment : Fragment(), KodeinAware, MatchingIncomeAdapter.OnItemClickListener {
    private lateinit var binding: FragmentSeeAllBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var slideFromRightAnimController: LayoutAnimationController
    private lateinit var fallDownAnimController: LayoutAnimationController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeeAllBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupAnimations()
        setupViews()
        listeners()
    }

    private fun setupViews() {
        binding.apply {
            toolbar.visibility = View.VISIBLE
            binding.tvToolbarTitle.text = "Matching Income Report"
            hitApis()
        }
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun hitApis() {
        getReport(
            GetDirectIncomeReq(
                apiname = "GetBinaryIncome",
                obj = com.app.ulife.creator.models.payoutManage.directIncome.Obj(
                    from = "",
                    to = "",
                    userid = "" + preferenceManager.userid
                ),
            )
        )
    }

    private fun getReport(req: GetDirectIncomeReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getDirectIncomeHistory = MutableLiveData()
        viewModel.getDirectIncomeHistory.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetMatchingIncomeRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            if (response.data.isNullOrEmpty()) {
                                containerEmpty.visibility = View.VISIBLE
                                rvList.visibility = View.GONE
                            } else {
                                containerEmpty.visibility = View.GONE
                                rvList.visibility = View.VISIBLE
                                rvList.apply {
                                    adapter = MatchingIncomeAdapter(
                                        context,
                                        response.data,
                                        this@MatchingIncomeFragment
                                    )
                                    layoutAnimation = fallDownAnimController
                                }
                            }
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            containerEmpty.visibility = View.VISIBLE
                            rvList.visibility = View.GONE
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    binding.apply {
                        containerEmpty.visibility = View.VISIBLE
                        rvList.visibility = View.GONE
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                binding.apply {
                    containerEmpty.visibility = View.VISIBLE
                    rvList.visibility = View.GONE
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getDirectIncomeHistory(req)
    }

    override fun onItemsClick() {
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(isVisible = false)
    }

    private fun setupAnimations() {
        slideFromRightAnimController = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.ll_slide_in_from_right
        )
        fallDownAnimController =
            AnimationUtils.loadLayoutAnimation(
                context,
                R.anim.ll_fall_down
            )
    }
}