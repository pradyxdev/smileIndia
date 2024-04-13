/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 25/02/23, 5:03 PM
 *
 */

package com.app.ulife.creator.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.ItemsWalletListBinding
import com.app.ulife.creator.models.wallet.history.Data
import kotlin.random.Random

class WalletListAdapter(
    var context: Context,
    private val list: List<Data>
) :
    RecyclerView.Adapter<WalletListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsWalletListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(private val binding: ItemsWalletListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(response: Data, position: Int) {
            binding.apply {
                val color =
                    Color.argb(
                        225,
                        Random.nextInt(180),
                        Random.nextInt(180),
                        Random.nextInt(180)
                    ) // darker random color
                ivIcon.setBackgroundColor(color)

                tvName.text = "" + response.Remark

                val transType: String = if (response.DrAmount != null && response.DrAmount != 0.0) {
                    "DEBIT"
                } else {
                    "CREDIT"
                }

                when (transType) {
                    "CREDIT" -> {
                        tvAmt.text = "" + response.CrAmount
                        tvType.text = "$transType"
                        tvType.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                    }

                    "DEBIT" -> {
                        tvAmt.text = "" + response.DrAmount
                        tvType.text = "$transType"
                        tvType.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    }
                }
            }
        }
    }
}
