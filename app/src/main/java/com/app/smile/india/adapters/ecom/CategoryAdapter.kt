/*
 * *
 *  * Created by Prady on 4/12/24, 5:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 10:43 AM
 *
 */

package com.app.smile.india.adapters.ecom

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.smile.india.R
import com.app.smile.india.databinding.ItemsCategoriesBinding
import com.app.smile.india.models.getCat.Data


class CategoryAdapter(
    var context: Context,
    private val list: List<Data>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onCatItemClick(response: Data)
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

    inner class ViewHolder(private val binding: ItemsCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: Data, position: Int) {
            binding.apply {
//                ivImg.setImageResource(response.img)
                ivImg.load("" + response?.CategoryImage) {
                    placeholder(R.drawable.logo)
                    error(R.drawable.logo)
                }
                tvName.text = response.CategoryName
                root.setOnClickListener {
                    listener.onCatItemClick(response)
                }
            }
        }
    }
}
