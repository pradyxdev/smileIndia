/*
 * *
 *  * Created by Prady on 8/23/24, 4:22 PM
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
import com.app.smile.india.adapters.ecom.OrderDetailsItemAdapter
import com.app.smile.india.databinding.FragmentOrderDetailslBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.getSaleItems.GetSaleItemsReq
import com.app.smile.india.models.getSaleItems.GetSaleItemsRes
import com.app.smile.india.models.getSaleReport.GetSaleReportRes
import com.app.smile.india.ui.userTypeActivities.customer.CustMainActivity
import com.app.smile.india.viewModels.SharedVM
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
    private lateinit var orderDetails: GetSaleReportRes.Data

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
        orderDetails = Gson().fromJson(arguments?.getString("orderDetails"), GetSaleReportRes.Data::class.java)

        binding.apply {
            tvOrderId.text = "#" + orderDetails?.InvoiceNo
            tvPaymentMode.text = "" + orderDetails?.PaymentMode
            date.text = "" + orderDetails?.InvoiceDate
            address.text = "" + orderDetails?.BillingAddress
            tvOrdStatus.text = "" + orderDetails?.Status

            tvShipName.text = "Name : " + orderDetails?.UserName
            tvShipNumber.text = "Mobile : +91-" + orderDetails?.UserId
//            tvShipEmail.text = "Email : " + orderDetails?.C_Email

            subtotal.text = "₹ ${orderDetails?.TotalAmount}"
            tvGst.text = "₹ ${orderDetails?.TotalGST}"
            totalAmount.text = "₹ ${orderDetails?.TotalAmount}"
        }

        getOrderItems(
            GetSaleItemsReq(
                apiname = "GetSaleItems", obj = GetSaleItemsReq.Obj(
                    invoiceno = "" + orderDetails?.InvoiceNo,
                    session = "2024-2025"
                )
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

    private fun getOrderItems(request: GetSaleItemsReq) {
        viewModel.getOrderItems = MutableLiveData()
        viewModel.getOrderItems.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetSaleItemsRes::class.java)
                if (response.status) {
                    val list = mutableListOf<GetSaleItemsRes.Data>()
                    for (i in response.data.indices) {
                        list.addAll(response.data)
                    }
                    cartListAdapter = OrderDetailsItemAdapter(requireActivity(), list.distinct())
                    binding.rvProduct.adapter = this@OrderDetailsFragment.cartListAdapter
                } else {
                    (activity as CustMainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as CustMainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getOrderItems(request)
    }
}