/*
 * *
 *  * Created by Prady on 6/3/23, 4:52 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 6/3/23, 4:49 PM
 *
 */

package com.app.ulife.creator.ui.userTypeActivities.customer.productActivity

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.ActivityProductDetailsBinding
import com.app.ulife.creator.factories.SharedVMF
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.helpers.PreferenceManager
import com.app.ulife.creator.models.CommonUserIdReq
import com.app.ulife.creator.models.UserIdObj
import com.app.ulife.creator.models.addToCart.AddToCartReq
import com.app.ulife.creator.models.addToCart.AddToCartRes
import com.app.ulife.creator.models.cart.GetCartRes
import com.app.ulife.creator.ui.userTypeActivities.customer.cartActivity.BagActivity
import com.app.ulife.creator.utils.toast
import com.app.ulife.creator.viewModels.SharedVM
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ProductDetailsActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var binding: ActivityProductDetailsBinding
    private var isLiked = false
    private var productId = ""
    private lateinit var productObj: com.app.ulife.creator.models.productList.Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        setContentView(binding.root)
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(this, factory)[SharedVM::class.java]
        setupViews()
        setupListeners()
        hitApis()
    }

    private fun setupViews() {
        val gson = Gson()
        productObj = gson.fromJson(
            intent.getStringExtra("identifier"),
            com.app.ulife.creator.models.productList.Data::class.java
        )

        productId = "" + productObj?.prodid

        binding.apply {
            ivImg.load("" + productObj?.img) {
                placeholder(R.drawable.logo)
                error(R.drawable.logo)
            }
            tvName.text = "" + productObj?.name
            tvMrp.text = "" + productObj?.mrp
            tvMrp.paintFlags = tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            tvSp.text = "" + productObj?.sp
            tvFinalPrice.text = "" + productObj?.sp
            tvCategory.text = "BV : " + productObj?.bv
            tvDesc.text = "" + productObj?.desc

            // chip stock
            chipStock.text = "${productObj?.AvailableStock} stock left"

            if (productObj.AvailableStock <= 0) {
                tvOos.visibility = View.VISIBLE
                btnAddCart.isEnabled = false
                btnBuyNow.isEnabled = false
            }

            // random values
            val randomSold = (0 until 300).random()
            chipSold.text = "Sold $randomSold"

            val randomRating = (0 until 10).random()
            val randomReview = (5 until 30).random()
            chipRating.text = "4.$randomRating ($randomReview Reviews) "
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left)
            }
            cardCart.setOnClickListener {
                startActivity(Intent(this@ProductDetailsActivity, BagActivity::class.java))
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left)
            }

            btnLike.setOnClickListener {
                isLiked = if (isLiked) {
                    btnLike.setIconResource(R.drawable.heart_outlined)
                    false
                } else {
                    btnLike.setIconResource(R.drawable.heart_filled)
                    true
                }
            }

            btnAddCart.setOnClickListener {
                addToCart(
                    AddToCartReq(
                        apiname = "AddToCart",
                        obj = com.app.ulife.creator.models.addToCart.Obj(
                            productid = "" + productId,
                            qty = "1",
                            userid = "" + preferenceManager.userid
                        )
                    ), "addCart"
                )
            }
            btnBuyNow.setOnClickListener {
                addToCart(
                    AddToCartReq(
                        apiname = "AddToCart",
                        obj = com.app.ulife.creator.models.addToCart.Obj(
                            productid = "" + productId,
                            qty = "1",
                            userid = "" + preferenceManager.userid
                        )
                    ), "openCart"
                )
            }
        }
    }

    private fun hitApis() {
//        getCart(
//            CommonUserIdReq(
//                apiname = "GetCart",
//                obj = UserIdObj(userId = "" + preferenceManager.userid)
//            )
//        )
    }

    private fun getCart(req: CommonUserIdReq) {
        viewModel.getCart = MutableLiveData()
        viewModel.getCart.observe(this) {
            try {
                val response = Gson().fromJson(it, GetCartRes::class.java)
                if (response != null) {
                    if (response.status) {
                        if (response.data.size > 9) {
                            binding.tvCartCount.text = "9+"
                        } else {
                            binding.tvCartCount.text = "" + response.data.size
                        }
                    } else {
                        binding.tvCartCount.text = "0"
                    }
                } else {
                    binding.tvCartCount.text = "0"
                }
            } catch (e: Exception) {
                binding.tvCartCount.text = "0"
                apiErrorDialog("$e")
            }
        }
        viewModel.getCart(req)
    }

    private fun addToCart(req: AddToCartReq, type: String) {
        viewModel.addToCart = MutableLiveData()
        viewModel.addToCart.observe(this) {
            try {
                val response = Gson().fromJson(it, AddToCartRes::class.java)
                if (response != null) {
                    if (response.status) {
                        when (type) {
                            "openCart" -> {
                                openBagActivity()
                            }

                            "addCart" -> {
                                applicationContext?.toast("Product has been added to your cart !")
//                                val snackBar = Snackbar
//                                    .make(
//                                        binding.root,
//                                        "Product has been added to your cart !",
//                                        Snackbar.LENGTH_LONG
//                                    )
//                                    .setAction("OPEN CART") {
//                                        openBagActivity()
//                                    }
//                                snackBar.show()

                                getCart(
                                    CommonUserIdReq(
                                        apiname = "GetCart",
                                        obj = UserIdObj(userId = "" + preferenceManager.userid)
                                    )
                                )
                            }

                            else -> {
                                println("Invalid cart request !")
                            }
                        }
                    } else {
                        apiErrorDialog(response.message)
                    }
                } else {
                    apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                Log.e("addToCartExc ", "$e")
                apiErrorDialog("$it\n$e")
            }
        }
        viewModel.addToCart(req)
    }

    private fun openBagActivity() {
        startActivity(
            Intent(
                this,
                BagActivity::class.java
            )
        )
        overridePendingTransition(
            R.anim.slide_in_from_right,
            R.anim.slide_out_left
        )
    }

    fun apiErrorDialog(errMsg: String) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_header_api_failed)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val title = dialog.findViewById<TextView>(R.id.tv_title)

        val subtitle = dialog.findViewById<TextView>(R.id.tv_sub_title)
        subtitle.text = errMsg

        val btnClose = dialog.findViewById<TextView>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.attributes = lp
    }

    override fun onResume() {
        super.onResume()
        getCart(
            CommonUserIdReq(
                apiname = "GetCart",
                obj = UserIdObj(userId = "" + preferenceManager.userid)
            )
        )
    }

    //    private fun setImageCardChecked(
//        card1: MaterialCardView,
//        card2: MaterialCardView,
//        card3: MaterialCardView,
//        card4: MaterialCardView
//    ) {
//        card1.isChecked = true
//        card1.strokeColor = ContextCompat.getColor(this, R.color.md_theme_tertiary)
//        card2.isChecked = false
//        card2.strokeColor = ContextCompat.getColor(this, R.color.transparent)
//        card3.isChecked = false
//        card3.strokeColor = ContextCompat.getColor(this, R.color.transparent)
//        card4.isChecked = false
//        card4.strokeColor = ContextCompat.getColor(this, R.color.transparent)
//    }
}