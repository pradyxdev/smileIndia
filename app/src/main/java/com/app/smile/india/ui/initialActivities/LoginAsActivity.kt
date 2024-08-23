/*
 * *
 *  * Created by Prady on 7/18/23, 6:28 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 6:28 PM
 *
 */

package com.app.smile.india.ui.initialActivities

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.app.smile.india.databinding.ActivityLoginAsBinding

class LoginAsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAsBinding
    private val sliderHandler = Handler()
    private val sliderRunnable = Runnable {
        binding.viewPagerImageSlider.currentItem = binding.viewPagerImageSlider.currentItem + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAsBinding.inflate(layoutInflater)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        setupSlider()
    }

    private fun setupSlider() {
//        val imagesList: MutableList<IntroSliderModel> = ArrayList()
//        imagesList.add(IntroSliderModel(R.drawable.bg_6, "Minimal, Affordable\nSilver Jewellery"))
//        imagesList.add(IntroSliderModel(R.drawable.bg_3, "1000+ premium designs\nhandcrafted for you"))
//        imagesList.add(IntroSliderModel(R.drawable.bg_1, "Trusted by 500,000+ divas\nacross India"))
//        imagesList.add(IntroSliderModel(R.drawable.bg_4, "Minimal, Affordable\nSilver Jewellery"))
//        imagesList.add(IntroSliderModel(R.drawable.bg_2, "1000+ premium designs\nhandcrafted for you"))
//        imagesList.add(IntroSliderModel(R.drawable.bg_5, "Trusted by 500,000+ divas\nacross India"))
//
//        binding.viewPagerImageSlider.adapter = IntroSliderAdapter(
//            imagesList, binding.viewPagerImageSlider
//        )
//        binding.viewPagerImageSlider.clipToPadding = false
//        binding.viewPagerImageSlider.clipChildren = false
//        binding.viewPagerImageSlider.offscreenPageLimit = 3
//        binding.viewPagerImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//
//        val compositePageTransformer = CompositePageTransformer()
//        compositePageTransformer.addTransformer(MarginPageTransformer(40))
//        compositePageTransformer.addTransformer { page, position ->
//            val r = 1 - abs(position)
//            page.scaleY = 0.85f + r * 0.15f
//        }
//        binding.viewPagerImageSlider.setPageTransformer(compositePageTransformer)
//        binding.viewPagerImageSlider.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                sliderHandler.removeCallbacks(sliderRunnable)
//                sliderHandler.postDelayed(sliderRunnable, 6000) //slide duration
//            }
//        })
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 1000)
    }
}