/*
 * *
 *  * Created by Prady on 4/12/24, 5:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/6/24, 5:34 PM
 *
 */

package com.app.ulife.creatoron.adapters.ecom

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.ItemsGridsBinding
import com.app.ulife.creatoron.models.productList.Data


class ItemsGridAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener,
    itemCounts: Int
) :
    RecyclerView.Adapter<ItemsGridAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsGridsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onGridItemsClick(s: String, response: Data)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    private val itemsCount = itemCounts
    override fun getItemCount(): Int {
        val counter: Int = when (itemsCount) {
            0 -> list.size
            else -> {
                if (itemsCount > list.size) {
                    list.size
                } else {
                    itemsCount
                }
            }
        }
        return counter
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private var isLiked = false

    inner class ViewHolder(private val binding: ItemsGridsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: Data, position: Int) {
            binding.apply {
                ivImg.load("" + response?.img) {
                    placeholder(R.drawable.logo)
                    error(R.drawable.logo)
                }
                tvName.text = "" + response?.name
//                tvCategory.text = response.category
                tvPrice.text = "" + response?.sp
                tvMrp.text = "MRP \u20B9 " + response?.mrp
                tvMrp.paintFlags = tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

//                btnLike.setOnClickListener {
//                    isLiked = if (isLiked) {
//                        btnLike.setIconResource(R.drawable.heart_outlined)
//                        false
//                    } else {
//                        btnLike.setIconResource(R.drawable.heart_filled)
//                        true
//                    }
//                }

                root.setOnClickListener {
                    listener.onGridItemsClick("open", response)
                }

                btnAddToCart.setOnClickListener {
                    listener.onGridItemsClick("add", response)
                }

                if (response.AvailableStock <= 0) {
                    tvOos.visibility = View.VISIBLE
                    btnAddToCart.isEnabled = false
                }
            }
        }
    }
}
