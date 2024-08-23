/*
 * *
 *  * Created by Prady on 8/23/24, 4:20 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/23/24, 4:16 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.reports.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.smile.india.R
import com.app.smile.india.adapters.ecom.OrderListAdapter
import com.app.smile.india.databinding.FragmentSeeAllBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.getSaleReport.GetSaleReportReq
import com.app.smile.india.models.getSaleReport.GetSaleReportRes
import com.app.smile.india.ui.userTypeActivities.customer.CustMainActivity
import com.app.smile.india.utils.getNavOptions
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class OrderHistoryFragment : Fragment(), KodeinAware, OrderListAdapter.OnItemClickListener {
    private lateinit var binding: FragmentSeeAllBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

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
        setupViews()
        listeners()
    }

    private fun setupViews() {
        binding.tvToolbarTitle.text = "My Order History"
        getOrderHistory(
            GetSaleReportReq(
                apiname = "GetSaleReport", obj = GetSaleReportReq.Obj(userid = "" + preferenceManager.userid)
            )
        )
    }

    override fun onResume() {
        super.onResume()
        (activity as CustMainActivity).setBottombarVisibility(isVisible = false)
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun getOrderHistory(req: GetSaleReportReq) {
        viewModel.getOrderHistory = MutableLiveData()
        viewModel.getOrderHistory.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetSaleReportRes::class.java)
                if (response.status) {
                    binding.apply {
                        rvList.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            adapter = OrderListAdapter(
                                context,
                                response.data,
                                this@OrderHistoryFragment
                            )
                        }
                        containerEmpty.visibility = View.GONE
                        rvList.visibility = View.VISIBLE
                    }
                } else {
                    binding.rvList.visibility = View.GONE
                    binding.containerEmpty.visibility = View.VISIBLE
                    (activity as CustMainActivity).apiErrorDialog(it)
                }
            } catch (e: Exception) {
                binding.rvList.visibility = View.GONE
                binding.containerEmpty.visibility = View.VISIBLE
                (activity as CustMainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getOrderHistory(req)
    }

    override fun onOrderItemClick(response: GetSaleReportRes.Data, type: String) {
        when (type) {
            "details" -> {
                val args = Bundle().apply {
                    putString("orderDetails", Gson().toJson(response))
                }
                findNavController().navigate(R.id.orderDetailsFragment, args, getNavOptions())
            }

            else -> context?.toast("Coming soon !")
        }
    }
}