/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/7/23, 3:05 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.reports.affiliates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.app.ulife.creatoron.adapters.AffiliatesAdapter
import com.app.ulife.creatoron.databinding.FragmentAffiliatesBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.affiliates.details.GetAffiliatesDetailsReq
import com.app.ulife.creatoron.models.affiliates.details.GetAffiliatesDetailsRes
import com.app.ulife.creatoron.models.affiliates.list.GetAffiliatesReq
import com.app.ulife.creatoron.models.affiliates.list.GetAffiliatesRes
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.utils.calculateNoOfColumns
import com.app.ulife.creatoron.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class AffiliatesFragment : Fragment(), KodeinAware, AffiliatesAdapter.OnItemClickListener {
    private lateinit var binding: FragmentAffiliatesBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAffiliatesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
        getAffiliatesList(
            GetAffiliatesReq(
                apiname = "GetAffliateType",
                obj = GetAffiliatesReq.Obj()
            )
        )
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(false)
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun getAffiliatesList(req: GetAffiliatesReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getAffiliatesList = MutableLiveData()
        viewModel.getAffiliatesList.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetAffiliatesRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.apply {
                            if (!response.data.isNullOrEmpty()) {
                                containerEmpty.visibility = View.GONE
                                rvList.visibility = View.VISIBLE
                                rvList.apply {
                                    val mNoOfColumns = context.calculateNoOfColumns(context, 120)
                                    val gridManager = GridLayoutManager(
                                        context,
                                        mNoOfColumns,
                                    )
                                    layoutManager = gridManager
                                    adapter = AffiliatesAdapter(
                                        context,
                                        response.data,
                                        this@AffiliatesFragment
                                    )
                                }
                            } else {
                                containerEmpty.visibility = View.VISIBLE
                                rvList.visibility = View.GONE
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
        viewModel.getAffiliatesList(req)
    }

    override fun onItemClick(response: GetAffiliatesRes.Data) {
        getAffiliatesDetails(
            GetAffiliatesDetailsReq(
                apiname = "AffliateRequest", obj = GetAffiliatesDetailsReq.Obj(
                    type = "${response.Name}",
                    userid = "" + preferenceManager.userid
                )
            )
        )
    }

    private fun getAffiliatesDetails(req: GetAffiliatesDetailsReq) {
        LoadingUtils.showDialog(context, isCancelable = false)
        viewModel.getAffiliatesDetails = MutableLiveData()
        viewModel.getAffiliatesDetails.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetAffiliatesDetailsRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        (activity as MainActivity).openWebIntent("" + response.data[0]?.Url)
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
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getAffiliatesDetails(req)
    }
}