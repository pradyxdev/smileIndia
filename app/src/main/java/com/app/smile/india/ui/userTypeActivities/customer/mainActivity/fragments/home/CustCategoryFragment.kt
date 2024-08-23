/*
 * *
 *  * Created by Prady on 8/23/24, 4:21 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/23/24, 3:02 PM
 *
 */

package com.app.smile.india.ui.userTypeActivities.customer.mainActivity.fragments.home

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
import com.app.smile.india.adapters.ecom.CategoryAdapter
import com.app.smile.india.databinding.FragmentSubCategoryBinding
import com.app.smile.india.factories.SharedVMF
import com.app.smile.india.helpers.Constants
import com.app.smile.india.helpers.PreferenceManager
import com.app.smile.india.models.getCat.Data
import com.app.smile.india.models.getCat.GetCategoryReq
import com.app.smile.india.models.getCat.GetCategoryRes
import com.app.smile.india.models.getCat.Obj
import com.app.smile.india.ui.userTypeActivities.customer.CustMainActivity
import com.app.smile.india.utils.AlertDialogUtil.apiAlertDialog
import com.app.smile.india.utils.LoadingUtils
import com.app.smile.india.utils.calculateNoOfColumns
import com.app.smile.india.utils.getNavOptions
import com.app.smile.india.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class CustCategoryFragment : Fragment(), KodeinAware, CategoryAdapter.OnItemClickListener {
    private lateinit var binding: FragmentSubCategoryBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

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
            tvTitle.text = "Category"
            getCategories(GetCategoryReq(apiname = "getCategory", obj = Obj()))
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as CustMainActivity).setBottombarVisibility(isVisible = true)
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun getCategories(req: GetCategoryReq) {
        LoadingUtils.showDialog(context, false)
        viewModel.getCategories = MutableLiveData()
        viewModel.getCategories.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetCategoryRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        binding.llNoData.visibility = View.GONE
                        binding.rvList.apply {
                            val mNoOfColumns = context.calculateNoOfColumns(context, 140)
                            val gridManager = GridLayoutManager(
                                context,
                                mNoOfColumns,
                            )
                            layoutManager = gridManager
                            adapter =
                                CategoryAdapter(context, response.data, this@CustCategoryFragment)
                        }
//                        when (response.data[0].Id) {
//                            1 -> {
//                                binding.rvCategory.apply {
//                                    adapter =
//                                        CategoryAdapter(context, response.data, this@HomeFragment)
//                                }
//                            }
//
//                            else -> (activity as MainActivity).apiErrorDialog("" + response?.message)
//                        }
                    } else {
                        LoadingUtils.hideDialog()
                        binding.llNoData.visibility = View.VISIBLE
                        context?.apiAlertDialog(
                            isError = true,
                            title = "",
                            subTitle = "${response?.message}",
                            action = {
                            })
                    }
                } else {
                    LoadingUtils.hideDialog()
                    binding.llNoData.visibility = View.VISIBLE
                    context?.apiAlertDialog(
                        isError = true,
                        title = "",
                        subTitle = "${Constants?.apiErrors}",
                        action = {
                        })
                }
            } catch (e: Exception) {
                LoadingUtils.hideDialog()
                binding.llNoData.visibility = View.VISIBLE
                context?.apiAlertDialog(
                    isError = true,
                    title = "$e",
                    subTitle = "$it",
                    action = {
                    })
                (activity as CustMainActivity).checkLoginSession("$it")
            }
        }
        viewModel.getCategories(req)
    }

    override fun onCatItemClick(response: Data) {
        val args = Bundle()
        args.putString("catId", "" + response.Id)
        args.putString("catName", response.CategoryName)
        findNavController().navigate(R.id.subCategoryFragment, args, getNavOptions())
    }
}