/*
 * *
 *  * Created by Prady on 8/3/23, 11:19 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 8/3/23, 11:14 AM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.reports.payout.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.smile.india.R
import com.app.smile.india.adapters.payout.LdcAdapter
import com.app.smile.india.databinding.FragmentLdcReportBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.EmptyRequest
import com.app.smile.india.models.Obj
import com.app.smile.india.models.payoutManage.ldc.GetLdcDatesRes
import com.app.smile.india.models.payoutManage.ldc.report.GetLdcReportReq
import com.app.smile.india.models.payoutManage.ldc.report.GetLdcReportRes
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class LdcFragment : Fragment(), KodeinAware, LdcAdapter.OnItemClickListener {
    private lateinit var binding: FragmentLdcReportBinding
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
        binding = FragmentLdcReportBinding.inflate(layoutInflater)
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
            binding.tvToolbarTitle.text = "LDC Report"
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
        getLdcDates(EmptyRequest(apiname = "LDCClosingDate", obj = Obj()))
    }

    private fun getLdcDates(req: EmptyRequest) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getLdcDates = MutableLiveData()
        viewModel.getLdcDates.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetLdcDatesRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            if (!response.data.isNullOrEmpty()) {
                                val list = mutableListOf<String>()
                                val listId = mutableListOf<String>()
                                list.clear()
                                listId.clear()

                                for (i in response.data.indices) {
                                    list.add("" + response.data[i]?.ClosingDate)
                                    listId.add("" + response.data[i]?.dailyId)
                                }

                                val adapter = ArrayAdapter(
                                    requireActivity(),
                                    android.R.layout.simple_list_item_1,
                                    list
                                )
                                actClosingDate.setAdapter(adapter)
                                actClosingDate.onItemClickListener =
                                    AdapterView.OnItemClickListener { parent, view, position, id ->
                                        getReport(
                                            GetLdcReportReq(
                                                apiname = "GetLDCIncome",
                                                obj = com.app.smile.india.models.payoutManage.ldc.report.Obj(
                                                    closingid = "" + listId[position],
                                                    userid = "" + preferenceManager.userid
                                                ),
                                            )
                                        )
                                    }
                            }
                        }
                    } else {
                        LoadingUtils.hideDialog()
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    LoadingUtils.hideDialog()
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getLdcDates(req)
    }

    private fun getReport(req: GetLdcReportReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getLdcReport = MutableLiveData()
        viewModel.getLdcReport.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetLdcReportRes::class.java)
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
                                    adapter = LdcAdapter(
                                        context,
                                        response.data,
                                        this@LdcFragment
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
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getLdcReport(req)
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