/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:36 AM
 *
 */

package com.app.ulife.creator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.ulife.creator.R
import com.app.ulife.creator.models.IntroSliderModel
import com.flaviofaria.kenburnsview.KenBurnsView

class IntroSliderAdapter(sliderItems: MutableList<IntroSliderModel>, viewPager2: ViewPager2) :
    RecyclerView.Adapter<IntroSliderAdapter.SliderViewHolder>() {
    private val sliderItems: List<IntroSliderModel>
    private val viewPager2: ViewPager2
    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container_2, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position])
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: KenBurnsView = itemView.findViewById(R.id.imageSlide)
        private val title: TextView = itemView.findViewById(R.id.tv_header)

        fun setImage(sliderItems: IntroSliderModel) {
            //use glide or picasso in case you get image from internet
            imageView.setImageResource(sliderItems.imageURL)
            title.text = sliderItems.title
//            Glide.with(itemView.context).load(sliderItems.imageURL)
//                .placeholder(R.drawable.logo)
//                .into(imageView)
        }
    }

    init {
        this.sliderItems = sliderItems
        this.viewPager2 = viewPager2
    }
}