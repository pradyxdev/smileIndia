/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 25/02/23, 5:03 PM
 *
 */

package com.app.prady.appbase.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.prady.appbase.R
import com.app.prady.appbase.databinding.ItemsGridsBinding
import com.app.prady.appbase.models.dummys.items.ItemsMD


class ItemsGridAdapter(
    var context: Context,
    private val list: List<ItemsMD>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<ItemsGridAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsGridsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onGridItemsClick()
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

    private var isLiked = false

    inner class ViewHolder(private val binding: ItemsGridsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: ItemsMD, position: Int) {
            binding.apply {
//                ivImg.setImageResource(response.img)
//                tvName.text = response.name
//                tvCategory.text = response.category
//                tvPrice.text = response.price

                btnLike.setOnClickListener {
                    isLiked = if (isLiked) {
                        btnLike.setIconResource(R.drawable.heart_outlined)
                        false
                    } else {
                        btnLike.setIconResource(R.drawable.heart_filled)
                        true
                    }
                }

                root.setOnClickListener {
                    listener.onGridItemsClick()
                }
            }
        }
    }
}
