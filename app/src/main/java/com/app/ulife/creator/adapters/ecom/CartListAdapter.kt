/*
 * *
 *  * Created by Prady on 4/12/24, 5:45 PM
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 4/4/24, 4:42 PM
 *
 */

package com.app.ulife.creator.adapters.ecom

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.ItemsCartBinding
import com.app.ulife.creator.helpers.Constants
import com.app.ulife.creator.models.cart.Data
import com.app.ulife.creator.utils.toast

class CartListAdapter(
    var context: Context,
    var list: MutableList<Data>,
    internal var listener: OnItemClickListener,
    internal var listener2: OnCounterClickListener
) :
    RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onDelete(
            productId: String
        )
    }

    private fun onDelete(
        productId: String
    ) {
        listener.onDelete(productId)
    }

    interface OnCounterClickListener {
        fun onCounterClick(data: Data, count: Int)
    }

    inner class ViewHolder(var binding: ItemsCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Data, position: Int) {
            binding.apply {
                image.load(Constants.imgBaseUrl + product?.ProductImage) {
                    placeholder(R.drawable.logo)
                    error(R.drawable.logo)
                }
                tvName.text = "" + product?.ProductName
                tvQty.text = "" + product?.Qty

                tvMrp.text = "" + product?.ProductMrp
                tvMrp.paintFlags = tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                tvProAmount.text = "" + product?.SP

                btnDelete.setOnClickListener {
                    onDelete("" + product?.id)
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, list.size)
                }

                plus.setOnClickListener {
                    incrementBtnClick(product, position)
                }
                minus.setOnClickListener {
                    if (tvQty.text.toString().toInt() > 1) {
                        decrementBtnClick(product, position)
                    } else {
                        context.toast("Can't be less than 1 !")
                    }
                }
            }
        }

        private fun incrementBtnClick(response: Data, position: Int) {
            binding.tvQty.text = (binding.tvQty.text.toString().toInt() + 1).toString()
            listener2.onCounterClick(
                response,
                binding.tvQty.text.toString().toInt()
            )
        }

        private fun decrementBtnClick(response: Data, position: Int) {
            binding.tvQty.text = (binding.tvQty.text.toString().toInt() - 1).toString()
            listener2.onCounterClick(
                response,
                binding.tvQty.text.toString().toInt()
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsCartBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
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
}


