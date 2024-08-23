/*
 * *
 *  * Created by Prady on 8/23/24, 4:22 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/23/24, 3:02 PM
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
import androidx.recyclerview.widget.GridLayoutManager
import com.app.smile.india.R
import com.app.smile.india.adapters.ecom.ItemsGridAdapter
import com.app.smile.india.databinding.FragmentSubCategoryBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.addToCart.AddToCartReq
import com.app.smile.india.models.addToCart.AddToCartRes
import com.app.smile.india.models.addToCart.Obj
import com.app.smile.india.models.productList.GetProductListReq
import com.app.smile.india.models.productList.GetProductListRes
import com.app.smile.india.ui.userTypeActivities.customer.CustMainActivity
import com.app.smile.india.ui.userTypeActivities.customer.cartActivity.BagActivity
import com.app.smile.india.ui.userTypeActivities.department.productActivity.ProductDetailsActivity
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.calculateNoOfColumns
import com.app.smile.india.utils.toast
import com.app.smile.india.viewModels.SharedVM
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SubCategoryListFragment : Fragment(), KodeinAware, ItemsGridAdapter.OnItemClickListener {
    private lateinit var binding: FragmentSubCategoryBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private var catId = ""
    private var subCatId = ""

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
            tvTitle.text = arguments?.getString("subCatName")
            catId = "" + arguments?.getString("catId")
            subCatId = "" + arguments?.getString("subCatId")

            getProdList(
                GetProductListReq(
                    apiname = "getProductList",
                    obj = com.app.smile.india.models.productList.Obj(
                        id = "" + catId,
                        subcatid = "" + subCatId,
                        name = "",
                        type = ""
                    )
                )
            )
        }
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
                            (activity as CustMainActivity).apiErrorDialog(response.message)
                        } else {
                            binding.apply {
                                llNoData.visibility = View.GONE
                                rvList.visibility = View.VISIBLE
                                binding.rvList.apply {
                                    val mNoOfColumns = context.calculateNoOfColumns(context, 140)
                                    val gridManager = GridLayoutManager(
                                        context,
                                        mNoOfColumns,
                                    )
                                    layoutManager = gridManager
                                    adapter =
                                        ItemsGridAdapter(context, response.data, this@SubCategoryListFragment, 0)
                                }
                            }
                        }
                    } else {
                        binding.apply {
                            llNoData.visibility = View.VISIBLE
                            rvList.visibility = View.GONE
                        }
                        LoadingUtils.hideDialog()
                        (activity as CustMainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        llNoData.visibility = View.VISIBLE
                        rvList.visibility = View.GONE
                    }
                    LoadingUtils.hideDialog()
                    (activity as CustMainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    llNoData.visibility = View.VISIBLE
                    rvList.visibility = View.GONE
                }
                LoadingUtils.hideDialog()
                (activity as CustMainActivity).apiErrorDialog("$e\n$it")
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
                        (activity as CustMainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as CustMainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as CustMainActivity).apiErrorDialog(e.toString())
            }
        }
        viewModel.addToCart(req)
    }
}