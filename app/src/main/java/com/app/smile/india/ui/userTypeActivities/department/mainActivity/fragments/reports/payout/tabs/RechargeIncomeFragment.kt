/*
 * *
 *  * Created by Prady on 8/3/23, 11:19 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 8/3/23, 11:14 AM
 *
 */

package com.app.smile.india.ui.userTypeActivities.department.mainActivity.fragments.reports.payout.tabs

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
import com.app.smile.india.R
import com.app.smile.india.adapters.payout.RechargeIncomeAdapter
import com.app.smile.india.databinding.FragmentSeeAllBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.payoutManage.rechargeIncome.GetRechargeIncomeReq
import com.app.smile.india.models.payoutManage.rechargeIncome.GetRechargeIncomeRes
import com.app.smile.india.ui.userTypeActivities.department.mainActivity.MainActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class RechargeIncomeFragment : Fragment(), KodeinAware, RechargeIncomeAdapter.OnItemClickListener {
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
            binding.tvToolbarTitle.text = "Recharge Income Report"
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
            GetRechargeIncomeReq(
                apiname = "GetRechargeIncome",
                obj = com.app.smile.india.models.payoutManage.rechargeIncome.Obj(
                    from = "",
                    lvl = "",
                    to = "",
                    type = "",
                    userid = "" + preferenceManager.userid
                ),
            )
        )
    }

    private fun getReport(req: GetRechargeIncomeReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getRechargeIncomeHistory = MutableLiveData()
        viewModel.getRechargeIncomeHistory.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetRechargeIncomeRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            containerEmpty.visibility = View.GONE
                            rvList.visibility = View.VISIBLE
                            rvList.apply {
                                adapter = RechargeIncomeAdapter(
                                    context,
                                    response.data,
                                    this@RechargeIncomeFragment
                                )
                                layoutAnimation = fallDownAnimController
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getRechargeIncomeHistory(req)
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