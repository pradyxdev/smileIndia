/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/7/23, 3:05 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.smile.india.R
import com.app.smile.india.adapters.ecom.ItemsGridAdapter
import com.app.smile.india.adapters.ecom.SubCategoryAdapter
import com.app.smile.india.databinding.FragmentSubCategoryBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.addToCart.AddToCartReq
import com.app.smile.india.models.addToCart.AddToCartRes
import com.app.smile.india.models.addToCart.Obj
import com.app.smile.india.models.getSubCategory.GetSubCategoryReq
import com.app.smile.india.models.getSubCategory.GetSubCategoryRes
import com.app.smile.india.models.productList.GetProductListReq
import com.app.smile.india.models.productList.GetProductListRes
import com.app.smile.india.ui.userTypeActivities.customer.cartActivity.BagActivity
import com.app.smile.india.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.smile.india.ui.userTypeActivities.customer.productActivity.ProductDetailsActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.calculateNoOfColumns
import com.app.smile.india.utils.getNavOptions
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.SharedVM
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SubCategoryFragment : Fragment(), KodeinAware, ItemsGridAdapter.OnItemClickListener,
    SubCategoryAdapter.OnItemClickListener {
    private lateinit var binding: FragmentSubCategoryBinding
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
        binding = FragmentSubCategoryBinding.inflate(layoutInflater)
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
            tvTitle.text = arguments?.getString("catName")
            catId = "" + arguments?.getString("catId")

            getSubCategory(
                GetSubCategoryReq(
                    apiname = "GetSubCategory",
                    obj = GetSubCategoryReq.Obj(categoryid = "$catId")
                )
            )
//            getProdList(
//                GetProductListReq(
//                    apiname = "getProductList",
//                    obj = com.app.smile.india.models.productList.Obj(
//                        id = "" + catId,
//                        name = "",
//                        type = "Cat"
//                    )
//                )
//            )
        }
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun getSubCategory(req: GetSubCategoryReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.getSubCategory = MutableLiveData()
        viewModel.getSubCategory.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetSubCategoryRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()

                        if (response.data.isNullOrEmpty()) {
                            (activity as MainActivity).apiErrorDialog(response.message)
                        } else {
                            binding.apply {
                                llNoData.visibility = View.GONE
                                rvList.visibility = View.VISIBLE

                                rvList.apply {
                                    val mNoOfColumns = context.calculateNoOfColumns(context, 140)
                                    val gridManager = GridLayoutManager(
                                        context,
                                        mNoOfColumns,
                                    )
                                    layoutManager = gridManager
                                    adapter = SubCategoryAdapter(
                                        context,
                                        response.data,
                                        this@SubCategoryFragment
                                    )
                                }
                            }
                        }
                    } else {
                        binding.apply {
                            llNoData.visibility = View.VISIBLE
                            rvList.visibility = View.GONE
                        }
                        LoadingUtils.hideDialog()
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        llNoData.visibility = View.VISIBLE
                        rvList.visibility = View.GONE
                    }
                    LoadingUtils.hideDialog()
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    llNoData.visibility = View.VISIBLE
                    rvList.visibility = View.GONE
                }
                LoadingUtils.hideDialog()
                (activity as MainActivity).apiErrorDialog("$e\n$it")
            }
        }
        viewModel.getSubCategory(req)
    }

    override fun onCatItemClick(response: GetSubCategoryRes.Data) {
        val args = Bundle()
        args.putString("catId", "" + response.CategoryId)
        args.putString("subCatId", "" + response.Id)
        args.putString("subCatName", "" + response.SubCategoryName)
        findNavController().navigate(R.id.subCategoryFragment, args, getNavOptions())
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

                        if (response.data.isNullOrEmpty()) {
                            (activity as MainActivity).apiErrorDialog(response.message)
                        } else {
                            binding.rvList.apply {
                                val mNoOfColumns = context.calculateNoOfColumns(context, 140)
                                val gridManager = GridLayoutManager(
                                    context,
                                    mNoOfColumns,
                                )
                                layoutManager = gridManager
                                adapter =
                                    ItemsGridAdapter(
                                        context,
                                        response.data,
                                        this@SubCategoryFragment,
                                        6
                                    )
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
        viewModel.getProductListReq(req)
    }

    override fun onGridItemsClick(
        s: String,
        response: com.app.smile.india.models.productList.Data
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