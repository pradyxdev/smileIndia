/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 25/02/23, 5:03 PM
 *
 */

package com.app.smile.india.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.smile.india.databinding.ItemsDashboardBinding
import com.app.smile.india.models.dashCount.Data


class DashboardItemsAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<DashboardItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsDashboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onDashboardItemsClick()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(private val binding: ItemsDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: Data, position: Int) {
            binding.apply {
//                val color =
//                    Color.argb(
//                        225,
//                        Random.nextInt(50),
//                        Random.nextInt(50),
//                        Random.nextInt(50)
//                    ) // darker random color
//                ivDashBg.setBackgroundColor(color)
//                ivImg.setImageResource(response.img)
//                ivImg.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
//                tvTitle.setTextColor(color)
                tvTitle.text = "" + response.name

//                tvCount.setTextColor(color)
                tvCount.text = "" + response.value

//                cardContainer.strokeColor = color

                cardContainer.setOnClickListener {
                    listener.onDashboardItemsClick()
                }
            }
        }
    }
}
