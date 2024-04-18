/*
 * *
 *  * Created by Prady on 4/8/23, 11:38 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/8/23, 11:24 AM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.fragments.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.app.ulife.creator.R
import com.app.ulife.creator.adapters.ecom.CategoryAdapter
import com.app.ulife.creator.adapters.ecom.ItemsGridAdapter
import com.app.ulife.creator.adapters.ecom.SliderAdapter
import com.app.ulife.creator.databinding.FragmentHomeBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.CommonUserIdReq
import com.app.ulife.creator.models.EmptyRequest
import com.app.ulife.creator.models.SliderItemsModel
import com.app.ulife.creator.models.UserIdObj
import com.app.ulife.creator.models.addToCart.AddToCartReq
import com.app.ulife.creator.models.addToCart.AddToCartRes
import com.app.ulife.creator.models.banner.GetBannerRes
import com.app.ulife.creator.models.cart.GetCartRes
import com.app.ulife.creator.models.getCat.Data
import com.app.ulife.creator.models.getCat.GetCategoryReq
import com.app.ulife.creator.models.getCat.GetCategoryRes
import com.app.ulife.creator.models.getCat.Obj
import com.app.ulife.creator.models.productList.GetProductListReq
import com.app.ulife.creator.models.productList.GetProductListRes
import com.app.ulife.creator.ui.SplashActivity
import com.app.ulife.creator.ui.userTypeActivities.customer.cartActivity.BagActivity
import com.app.ulife.creator.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creator.ui.userTypeActivities.customer.productActivity.ProductDetailsActivity
import com.app.ulife.creator.utils.LoadingUtils
import com.app.ulife.creator.utils.calculateNoOfColumns
import com.app.ulife.creator.utils.getNavOptions
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import kotlin.math.abs

class HomeFragment : Fragment(), KodeinAware, CategoryAdapter.OnItemClickListener,
    ItemsGridAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private val sliderHandler = Handler()
    private val sliderRunnable = Runnable {
        binding.viewPagerImageSlider.currentItem = binding.viewPagerImageSlider.currentItem + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { // in here you can do logic when backPress is clicked
                showExitDialog()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        setupListeners()
        hitApis()
    }

    private fun setupViews() {
//        setHasOptionsMenu(true)
        preferenceManager.loggedIn = "true"
//        showViewStub()
//        setupRandomSearchTxt()
    }

    private fun setupListeners() {
        binding.apply {
            btnHamburger.setOnClickListener {
                (activity as MainActivity).openDrawer()
            }

            cardCart.setOnClickListener {
                startActivity(Intent(requireActivity(), BagActivity::class.java))
                activity?.overridePendingTransition(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_left
                )
            }
        }
    }

    private fun hitApis() {
        getCategories(GetCategoryReq(apiname = "getCategory", obj = Obj()))
        setupSlider(EmptyRequest(apiname = "getBanner", obj = com.app.ulife.creator.models.Obj()))
        getArrival(
            GetProductListReq(
                apiname = "getProductList",
                obj = com.app.ulife.creator.models.productList.Obj(id = "", name = "", type = "All")
            )
        )
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
                        binding.rvCategory.apply {
                            adapter =
                                CategoryAdapter(context, response.data, this@HomeFragment)
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
        viewModel.getCategories(req)
    }

    override fun onCatItemClick(response: Data) {
        val args = Bundle()
        args.putString("catId", "" + response.catid)
        args.putString("catName", response.CategoryName)
        findNavController().navigate(R.id.categoryFragment, args, getNavOptions())
    }

    private fun getArrival(req: GetProductListReq) {
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
                                ItemsGridAdapter(context, response.data, this@HomeFragment, 6)
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
                Log.e("getArrivalExc ", "$e")
                (activity as MainActivity).apiErrorDialog("$e")
            }
        }
        viewModel.getProductListReq(req)
    }

    override fun onGridItemsClick(
        s: String,
        response: com.app.ulife.creator.models.productList.Data
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
                        obj = com.app.ulife.creator.models.addToCart.Obj(
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

                        getCart(
                            CommonUserIdReq(
                                apiname = "GetCart",
                                obj = UserIdObj(userId = "" + preferenceManager.userid)
                            )
                        )
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

    private fun getCart(req: CommonUserIdReq) {
        viewModel.getCart = MutableLiveData()
        viewModel.getCart.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetCartRes::class.java)
                if (response != null) {
                    if (response.status) {
                        if (response.data.size > 9) {
                            binding.tvCartCount.text = "9+"
//                            (activity as MainActivity).setNavbarBadge(response.totalProducts.toString())
                        } else {
                            binding.tvCartCount.text = "" + response.data.size
//                            (activity as MainActivity).setNavbarBadge(response.totalProducts.toString())
                        }
                    } else {
                        binding.tvCartCount.text = "0"
                    }
                } else {
                    binding.tvCartCount.text = "0"
//                    (activity as MainActivity).setNavbarBadge("0")
                }
            } catch (e: Exception) {
                binding.tvCartCount.text = "0"
                (activity as MainActivity).apiErrorDialog("$it\n$e")
//                (activity as MainActivity).setNavbarBadge("0")
            }
        }
        viewModel.getCart(req)
    }

    private fun setupSlider(emptyRequest: EmptyRequest) {
        LoadingUtils.showDialog(context, false)
        viewModel.emptyRequestCommon = MutableLiveData()
        viewModel.emptyRequestCommon.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetBannerRes::class.java)
                if (response != null) {
                    if (response.status) {
                        LoadingUtils.hideDialog()
                        val imagesList: MutableList<SliderItemsModel> = ArrayList()
                        imagesList.clear()
                        for (i in response.data.indices) {
                            imagesList.add(SliderItemsModel("" + response.data[i]?.image))
                        }

                        binding.apply {
                            viewPagerImageSlider.adapter = SliderAdapter(
                                imagesList,
                                viewPagerImageSlider,
                            )
                            viewPagerImageSlider.clipToPadding = false
                            viewPagerImageSlider.clipChildren = false
                            viewPagerImageSlider.offscreenPageLimit = 3
                            viewPagerImageSlider.getChildAt(0).overScrollMode =
                                RecyclerView.OVER_SCROLL_NEVER

                            val compositePageTransformer = CompositePageTransformer()
                            compositePageTransformer.addTransformer(MarginPageTransformer(40))
                            compositePageTransformer.addTransformer { page, position ->
                                val r = 1 - abs(position)
                                page.scaleY = 0.85f + r * 0.15f
                            }
                            viewPagerImageSlider.setPageTransformer(compositePageTransformer)
                            viewPagerImageSlider.registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    super.onPageSelected(position)
                                    sliderHandler.removeCallbacks(sliderRunnable)
                                    sliderHandler.postDelayed(
                                        sliderRunnable,
                                        4000
                                    ) // slide duration
                                }
                            })
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
        viewModel.emptyRequestCommon(emptyRequest)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 500)
        (activity as MainActivity).setBottombarVisibility(isVisible = true)

        getCart(
            CommonUserIdReq(
                apiname = "GetCart",
                obj = UserIdObj(userId = "" + preferenceManager.userid)
            )
        )
    }

    private fun navigateToComingSoon(type: String) {
        /* Setting the animation for the navigation. */
        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(R.anim.slide_in_from_right).setExitAnim(R.anim.slide_out_to_right)
            .setPopEnterAnim(R.anim.slide_in_from_left).setPopExitAnim(R.anim.slide_out_left)

        val args = Bundle()
        args.putString("type", type)

        findNavController().navigate(R.id.comingSoonFragment, args, navBuilder.build())
    }

    private fun showExitDialog() {
        CFAlertDialog.Builder(context).setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setHeaderView(R.layout.dialog_header_logout).setTitle("Exit App !!")
            .setMessage("Are you sure you want to exit from this app ?").addButton(
                "Yes",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                requireActivity().finishAffinity()
                dialog.dismiss()
            }.addButton(
                "No",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }.setCancelable(true).show()
    }

//    override fun onPrepareOptionsMenu(menu: Menu) {
//        val profileMenuItem = menu.findItem(R.id.menu_profile)
//        val rootView = profileMenuItem.actionView as FrameLayout?
//        val profileImg = rootView!!.findViewById<View>(R.id.iv_profile_img) as ImageView
//        Glide.with(requireContext()).load(preferenceManager.profileImgUrl)
//            .placeholder(R.drawable.dummy_user).into(profileImg)
//
//        rootView.setOnClickListener {
//            onOptionsItemSelected(profileMenuItem)
//        }
//        return super.onPrepareOptionsMenu(menu)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        menu.clear() // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.home_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_profile -> {
//                val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
//                findNavController().navigate(action)
//            }
//            R.id.menu_logout -> {
//                showLogoutDialog()
//            }
//            R.id.menu_exit -> {
//                showExitDialog()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun showLogoutDialog() {
        CFAlertDialog.Builder(context).setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setHeaderView(R.layout.dialog_header_logout).setTitle("LOGOUT OUT !!")
            .setMessage("Are you sure you want to log out from the app ?").addButton(
                "Yes",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                preferenceManager.clear()
                startActivity(Intent(requireActivity(), SplashActivity::class.java))
                requireActivity().finishAffinity()
                dialog.dismiss()
            }.addButton(
                "No",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
            ) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }.setCancelable(true).show()
    }

//    private fun showViewStub() {
//        if (binding.viewStub == null) {
//        }
//        binding.viewStub.visibility = View.VISIBLE
//    }
//
//    private fun hideViewStub() {
//        if (binding.viewStub == null) {
//            return
//        }
//        binding.viewStub.visibility = View.GONE
//    }
}