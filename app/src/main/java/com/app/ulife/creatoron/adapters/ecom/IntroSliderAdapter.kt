/*
 * *
 *  * Created by Prady on 4/12/24, 5:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/1/24, 4:57 PM
 *
 */

package com.app.ulife.creatoron.adapters.ecom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.models.IntroSliderModel
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