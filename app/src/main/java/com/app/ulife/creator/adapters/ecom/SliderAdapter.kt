/*
 * *
 *  * Created by Prady on 4/12/24, 5:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 10:52 AM
 *
 */

package com.app.ulife.creator.adapters.ecom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.app.ulife.creator.R
import com.app.ulife.creator.models.SliderItemsModel

class SliderAdapter(sliderItems: MutableList<SliderItemsModel>, viewPager2: ViewPager2) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    private val sliderItems: List<SliderItemsModel>
    private val viewPager2: ViewPager2
    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container, parent, false
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
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlide)

        fun setImage(sliderItems: SliderItemsModel) {
            //use glide or picasso in case you get image from internet
//            imageView.setImageResource(sliderItems.imageURL)
            imageView.load("" + sliderItems?.imageURL) {
                placeholder(R.drawable.logo)
                error(R.drawable.logo)
            }
        }
    }

    init {
        this.sliderItems = sliderItems
        this.viewPager2 = viewPager2
    }
}