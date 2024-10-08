/*
 * *
 *  * Created by Prady on 4/12/24, 5:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/10/24, 2:15 PM
 *
 */

package com.app.smile.india.adapters.ecom

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.smile.india.R
import com.app.smile.india.databinding.ItemsOrderDetailsBinding
import com.app.smile.india.models.getSaleItems.GetSaleItemsRes

class OrderDetailsItemAdapter(var context: Context, var list: List<GetSaleItemsRes.Data>) :
    RecyclerView.Adapter<OrderDetailsItemAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemsOrderDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsOrderDetailsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            productName.text = "" + list[position]?.ProductName
            tvSize.text = "QTY : " + list[position]?.Quantity
            cutprice.text = "" + list[position]?.MRP
            cutprice.paintFlags = cutprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvProAmount.text = "" + list[position]?.TotalAmount
//
            image.load("" + list[position]?.ProductImage) {
                placeholder(R.drawable.logo)
                error(R.drawable.logo)
            }
        }
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
}