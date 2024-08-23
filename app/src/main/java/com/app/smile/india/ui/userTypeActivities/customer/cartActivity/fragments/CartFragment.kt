/*
 * *
 *  * Created by Prady on 8/23/24, 4:24 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/23/24, 2:01 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.cartActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.smile.india.R
import com.app.smile.india.adapters.ecom.CartListAdapter
import com.app.smile.india.databinding.FragmentCartBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.CommonUserIdReq
import com.app.smile.india.models.UserIdObj
import com.app.smile.india.models.addToCart.AddToCartReq
import com.app.smile.india.models.addToCart.AddToCartRes
import com.app.smile.india.models.addToCart.Obj
import com.app.smile.india.models.cart.Data
import com.app.smile.india.models.cart.GetCartRes
import com.app.smile.india.models.deleteCart.DeleteCartReq
import com.app.smile.india.models.deleteCart.DeleteCartRes
import com.app.smile.india.ui.userTypeActivities.customer.cartActivity.BagActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.getNavOptions
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class CartFragment : Fragment(), KodeinAware, CartListAdapter.OnItemClickListener,
    CartListAdapter.OnCounterClickListener {
    private lateinit var binding: FragmentCartBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private val emptyList = mutableListOf("", "", "", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
        hitApi()
    }

    private fun setupViews() {
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                activity?.finish()
            }
            btnCheckout.setOnClickListener {
                val args = Bundle()
                args.putString("mrp", "" + tvMrp.text)
                args.putString("discount", "" + tvDiscount.text)
                args.putString("finalAmt", "" + tvFinalPrice.text)
                findNavController().navigate(R.id.checkoutFragment, args, getNavOptions())
            }
        }
    }

    private fun hitApi() {
        getCart(
            CommonUserIdReq(
                apiname = "GetCart",
                obj = UserIdObj(userId = "" + preferenceManager.userid)
            )
        )
    }

    private fun getCart(req: CommonUserIdReq) {
        LoadingUtils.showDialog(requireContext(), isCancelable = false)
        viewModel.getCart = MutableLiveData()
        viewModel.getCart.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetCartRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()

                        if (response.data.isNullOrEmpty()) {
                            binding.apply {
                                llSummary.visibility = View.GONE
                                llNoData.visibility = View.VISIBLE
                            }
                        } else {
                            binding.apply {
                                llSummary.visibility = View.VISIBLE
                                llNoData.visibility = View.GONE

                                rvList.apply {
                                    adapter = CartListAdapter(
                                        context,
                                        response.data as MutableList<Data>,
                                        this@CartFragment,
                                        this@CartFragment
                                    )
                                }

                                var mrp: Double
                                var qty: Double
                                var finalMrp = 0.0

                                var sp: Double
                                var finalSp = 0.0
                                for (i in response.data.indices) {
                                    mrp = response.data[i].ProductMrp.toString().toDouble()
                                    qty = response.data[i].Qty.toString().toDouble()
                                    finalMrp += mrp * qty

                                    sp = response.data[i].SP
                                    finalSp += sp * qty
                                }

                                tvDiscount.text = "" + (finalMrp - finalSp)
                                tvMrp.text = finalMrp.toString()
                                tvFinalPrice.text = finalSp.toString()
                            }
                        }
                    } else {
                        binding.apply {
                            llSummary.visibility = View.GONE
                            llNoData.visibility = View.VISIBLE
                        }
                        (activity as BagActivity).apiErrorDialog("${response.message}")
                    }
                } else {
                    binding.apply {
                        llSummary.visibility = View.GONE
                        llNoData.visibility = View.VISIBLE
                    }
                    LoadingUtils.hideDialog()
                    (activity as BagActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    llSummary.visibility = View.GONE
                    llNoData.visibility = View.VISIBLE
                }
                LoadingUtils.hideDialog()
                (activity as BagActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getCart(req)
    }

    override fun onDelete(productId: String) {
        deleteCart(
            DeleteCartReq(
                apiname = "Remove_Cart",
                obj = com.app.smile.india.models.deleteCart.Obj(
                    id = "" + productId,
                    userid = "" + preferenceManager.userid
                )
            )
        )
    }

    private fun deleteCart(req: DeleteCartReq) {
        viewModel.deleteCart = MutableLiveData()
        viewModel.deleteCart.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, DeleteCartRes::class.java)
                if (response != null) {
                    if (response.status) {
                        when (response.data[0].Id) {
                            0 -> context?.toast("" + response.data[0]?.MSG)
                            else -> {
                                context?.toast("" + response.data[0]?.MSG)
                                getCart(
                                    CommonUserIdReq(
                                        apiname = "GetCart",
                                        obj = UserIdObj(userId = "" + preferenceManager.userid)
                                    )
                                )
                            }
                        }
                    } else {
                        (activity as BagActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as BagActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as BagActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.deleteCart(req)
    }

    override fun onCounterClick(
        data: Data,
        count: Int
    ) {
        addToCart(
            AddToCartReq(
                apiname = "AddToCart",
                obj = Obj(
                    productid = "" + data.ProductId,
                    qty = "$count",
                    userid = "" + preferenceManager.userid
                )
            )
        )
    }

    private fun addToCart(req: AddToCartReq) {
        viewModel.addToCart = MutableLiveData()
        viewModel.addToCart.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, AddToCartRes::class.java)
                if (response != null) {
                    if (response.status) {
                        getCart(
                            CommonUserIdReq(
                                apiname = "GetCart",
                                obj = UserIdObj(userId = "" + preferenceManager.userid)
                            )
                        )
                    } else {
                        (activity as BagActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as BagActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as BagActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.addToCart(req)
    }
}