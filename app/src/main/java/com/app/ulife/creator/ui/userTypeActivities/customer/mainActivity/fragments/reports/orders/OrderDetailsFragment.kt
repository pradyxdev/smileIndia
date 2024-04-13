/*
 * *
 *  * Created by Prady on 4/6/24, 1:32 PM
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
import com.app.ulife.creator.adapters.ecom.OrderDetailsItemAdapter
import com.app.ulife.creator.databinding.FragmentOrderDetailslBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.orderHistory.Data
import com.app.ulife.creator.models.orderItems.Obj
import com.app.ulife.creator.models.orderItems.OrderItemsReq
import com.app.ulife.creator.models.orderItems.OrderItemsRes
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class OrderDetailsFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentOrderDetailslBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var cartListAdapter: OrderDetailsItemAdapter
    private lateinit var orderDetails: Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailslBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
//        binding.tvTitle.text = "" + getString(R.string.my_order_details)
        orderDetails = Gson().fromJson(arguments?.getString("orderDetails"), Data::class.java)

        binding.apply {
            tvOrderId.text = "#" + orderDetails?.InvNo
            tvPaymentMode.text = "" + orderDetails?.PayMode
            date.text = "" + orderDetails?.BillingDate
            address.text = "" + orderDetails?.BillingAdd
            tvOrdStatus.text = "" + orderDetails?.Status

            tvShipName.text = "Name : " + orderDetails?.C_Name
            tvShipNumber.text = "Mobile : +91-" + orderDetails?.C_Mobile
            tvShipEmail.text = "Email : " + orderDetails?.C_Email

            subtotal.text = "₹ ${orderDetails?.TaxableAmt}"
            tvGst.text = "₹ ${orderDetails?.TotalGst}"
            totalAmount.text = "₹ ${orderDetails?.Amount}"
        }

        getOrderItems(
            OrderItemsReq(
                apiname = "GetOrderItems",
                obj = Obj(orderno = "" + orderDetails?.InvNo)
            )
        )
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun getOrderItems(request: OrderItemsReq) {
        viewModel.getOrderItems = MutableLiveData()
        viewModel.getOrderItems.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, OrderItemsRes::class.java)
                if (response.status) {
                    val list = mutableListOf<com.app.ulife.creator.models.orderItems.Data>()
                    for (i in response.data.indices) {
                        list.addAll(response.data)
                    }
                    cartListAdapter = OrderDetailsItemAdapter(requireActivity(), list.distinct())
                    binding.rvProduct.adapter = this@OrderDetailsFragment.cartListAdapter
                } else {
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getOrderItems(request)
    }
}