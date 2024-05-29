/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/7/23, 3:05 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.adapters.ecom.ItemsGridAdapter
import com.app.ulife.creatoron.databinding.FragmentCategoryBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.addToCart.AddToCartReq
import com.app.ulife.creatoron.models.addToCart.AddToCartRes
import com.app.ulife.creatoron.models.addToCart.Obj
import com.app.ulife.creatoron.models.productList.GetProductListReq
import com.app.ulife.creatoron.models.productList.GetProductListRes
import com.app.ulife.creatoron.ui.userTypeActivities.customer.cartActivity.BagActivity
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.ui.userTypeActivities.customer.productActivity.ProductDetailsActivity
import com.app.ulife.creatoron.utils.LoadingUtils
import com.app.ulife.creatoron.utils.calculateNoOfColumns
import com.app.ulife.creatoron.utils.toast
import com.app.ulife.creatoron.viewModels.SharedVM
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class CategoryFragment : Fragment(), KodeinAware, ItemsGridAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCategoryBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var catId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
    }

    private fun setupViews() {
        binding.apply {
            tvToolbarTitle.text = arguments?.getString("catName")
            catId = "" + arguments?.getString("catId")

            getProdList(
                GetProductListReq(
                    apiname = "getProductList",
                    obj = com.app.ulife.creatoron.models.productList.Obj(
                        id = "" + catId,
                        name = "",
                        type = "Cat"
                    )
                )
            )
        }
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun getProdList(req: GetProductListReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.getProductListReq = MutableLiveData()
        viewModel.getProductListReq.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetProductListRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()

                        binding.rvArrival.apply {
                            val mNoOfColumns = context.calculateNoOfColumns(context, 140)
                            val gridManager = GridLayoutManager(
                                context,
                                mNoOfColumns,
                            )
                            layoutManager = gridManager
                            adapter =
                                ItemsGridAdapter(context, response.data, this@CategoryFragment, 6)
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
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getProductListReq(req)
    }

    override fun onGridItemsClick(
        s: String,
        response: com.app.ulife.creatoron.models.productList.Data
    ) {
        when (s) {
            "open" -> {
                val gson = Gson()
                startActivity(
                    Intent(
                        requireActivity(),
                        ProductDetailsActivity::class.java
                    ).putExtra("identifier", gson.toJson(response))
                )
                activity?.overridePendingTransition(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_left
                )
            }

            "add" -> {
                addToCart(
                    AddToCartReq(
                        apiname = "AddToCart",
                        obj = Obj(
                            productid = "" + response.prodid,
                            qty = "1",
                            userid = "" + preferenceManager.userid
                        )
                    )
                )
            }

            else -> context?.toast("Action undefined !")
        }
    }

    private fun addToCart(req: AddToCartReq) {
        viewModel.addToCart = MutableLiveData()
        viewModel.addToCart.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, AddToCartRes::class.java)
                if (response != null) {
                    if (response.status) {
                        val snackBar = Snackbar
                            .make(
                                binding.root,
                                "Product has been added to your cart !",
                                Snackbar.LENGTH_LONG
                            )
                            .setAction("OPEN CART") {
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        BagActivity::class.java
                                    )
                                )
                                activity?.overridePendingTransition(
                                    R.anim.slide_in_from_right,
                                    R.anim.slide_out_left
                                )
                            }
                        snackBar.show()

//                        getCart(
//                            CommonUserIdReq(
//                                apiname = "GetCart",
//                                obj = UserIdObj(userId = "" + preferenceManager.userid)
//                            )
//                        )
                    } else {
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as MainActivity).apiErrorDialog(e.toString())
            }
        }
        viewModel.addToCart(req)
    }
}