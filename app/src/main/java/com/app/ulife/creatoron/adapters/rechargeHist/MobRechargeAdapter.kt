/*
 * *
 *  * Created by Prady on 25/02/23, 5:23 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 25/02/23, 5:03 PM
 *
 */

package com.app.ulife.creatoron.adapters.rechargeHist

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.ItemsWalletListBinding
import com.app.ulife.creatoron.models.paysprint.history.mobHistory.Data
import com.app.ulife.creatoron.utils.mask
import kotlin.random.Random

class MobRechargeAdapter(
    var context: Context,
    private val list: List<Data>
) :
    RecyclerView.Adapter<MobRechargeAdapter.ViewHolder>() {

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

                tvDate.text = "" + response?.EntryDate

                val custNumber = response?.CaNumber.toString().mask()
//                tvName.text = "${response?.OperatorName} : ${response?.PlanDescription}\nReference Id : ${response?.ReferenceId}"
                tvName.text =
                    "${response?.OperatorName} : +91-$custNumber\nReference Id : ${response?.ReferenceId}"

                val transType: String = if (response.Status != null && response.Status != 0) {
                    "SUCCESS"
                } else {
                    "FAILURE"
                }

                tvType.text = "$transType"

                when (transType) {
                    "SUCCESS" -> {
                        tvAmt.text = "" + response.Amount
                        tvType.text = "$transType"
                        tvType.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                    }

                    "FAILURE" -> {
                        tvAmt.text = "" + response.Amount
                        tvType.text = "$transType"
                        tvType.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    }
                }
            }
        }
    }
}
