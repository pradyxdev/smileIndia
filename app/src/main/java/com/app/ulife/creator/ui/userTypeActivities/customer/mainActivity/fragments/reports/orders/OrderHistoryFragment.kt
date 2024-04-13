/*
 * *
 *  * Created by Prady on 4/6/24, 12:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 12:06 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.reports.orders

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
import com.app.ulife.creator.R
import com.app.ulife.creator.adapters.ecom.OrderListAdapter
import com.app.ulife.creator.databinding.FragmentSeeAllBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.orderHistory.Data
import com.app.ulife.creator.models.orderHistory.Obj
import com.app.ulife.creator.models.orderHistory.OrderHistoryReq
import com.app.ulife.creator.models.orderHistory.OrderHistoryRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.utils.getNavOptions
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
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
            OrderHistoryReq(
                apiname = "GetOrderDetail",
                obj = Obj(orderno = "", userid = "" + preferenceManager.userid)
            )
        )
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottombarVisibility(isVisible = false)
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun getOrderHistory(req: OrderHistoryReq) {
        viewModel.getOrderHistory = MutableLiveData()
        viewModel.getOrderHistory.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, OrderHistoryRes::class.java)
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
                    (activity as MainActivity).apiErrorDialog(it)
                }
            } catch (e: Exception) {
                binding.rvList.visibility = View.GONE
                binding.containerEmpty.visibility = View.VISIBLE
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getOrderHistory(req)
    }

    override fun onOrderItemClick(response: Data, type: String) {
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