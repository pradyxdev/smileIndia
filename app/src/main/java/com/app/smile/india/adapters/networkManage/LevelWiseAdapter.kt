/*
 * *
 *  * Created by Prady on 4/10/24, 5:06 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 5:03 PM
 *
 */

package com.app.smile.india.adapters.networkManage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.smile.india.R
import com.app.smile.india.databinding.ItemsTable4Binding
import com.app.smile.india.models.networkManage.levelWise.CustomData
import com.app.smile.india.utils.getColorFromAttr

class LevelWiseAdapter(
    var context: Context,
    private val list: MutableList<CustomData>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<LevelWiseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsTable4Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemsClick()
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

    inner class ViewHolder(private val binding: ItemsTable4Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(response: CustomData, position: Int) {
            binding.apply {
                if (position == 0) {
                    llContainer.setBackgroundColor(context.getColorFromAttr(android.R.attr.colorPrimary))
                    tvH1.setTextColor(ContextCompat.getColor(context, R.color.white))
                    tvH2.setTextColor(ContextCompat.getColor(context, R.color.white))
                    tvH3.setTextColor(ContextCompat.getColor(context, R.color.white))
                    tvH4.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                tvH1.text = "${response?.levelcount}"
                tvH2.text = "${response?.active}"
                tvH3.text = "${response?.deactive}"

                val active = response?.active.toString().toInt()
                val deactive = response?.deactive.toString().toInt()

                tvH4.text = "${active + deactive}"
            }
        }
    }
}
