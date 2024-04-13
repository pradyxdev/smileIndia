/*
 * *
 *  * Created by Prady on 5/1/23, 12:26 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/3/23, 4:17 PM
 *
 */

package com.app.ulife.creator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creator.R
import com.app.ulife.creator.databinding.ItemsDthPlanListBinding
import com.app.ulife.creator.models.bbpsRecharge.dthPlan.PDetial
import com.app.ulife.creator.models.bbpsRecharge.dthPlan.Price
import com.app.ulife.creator.utils.toast

class DthPlanListAdapter(
    var context: Context,
    private val planList: MutableList<PDetial>,
    private val priceList: MutableList<Price>,
    internal var listener: OnItemClickListener
) :
    RecyclerView.Adapter<DthPlanListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsDthPlanListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(rs: String, desc: String, typesId: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(planList[position], position)
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(private val binding: ItemsDthPlanListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(response: PDetial, position: Int) {
            binding.apply {
                tvPkgName.text = "" + response?.packageName
                tvDesc.text = "" + response?.pDescription

                var monthly = ""
                var quarterly = ""
                var halfYearly = ""
                var yearly = ""

                for (i in priceList.indices) {
                    if (response.pDetials_Id == priceList[i].pDetials_Id) {
                        monthly = "" + priceList[i]?.monthly.toString().ifEmpty { "N/A" }
                        quarterly = "" + priceList[i]?.quarterly.toString().ifEmpty { "N/A" }
                        halfYearly = "" + priceList[i]?.halfYearly.toString().ifEmpty { "N/A" }
                        yearly = "" + priceList[i]?.yearly.toString().ifEmpty { "N/A" }
                    }
                }

                if (!monthly.equals("N/A")) {
                    chipMonthly.text = "Monthly \u20B9$monthly"
                } else {
                    chipMonthly.visibility = View.GONE
                    monthly = "0"
                }

                if (!quarterly.equals("N/A")) {
                    chipQuarterly.text = "Monthly \u20B9$quarterly"
                } else {
                    chipQuarterly.visibility = View.GONE
                    quarterly = "0"
                }

                if (!halfYearly.equals("N/A")) {
                    chipHalfYearly.text = "Monthly \u20B9$halfYearly"
                } else {
                    chipHalfYearly.visibility = View.GONE
                    halfYearly = "0"
                }

                if (!yearly.equals("N/A")) {
                    chipYearly.text = "Monthly \u20B9$yearly"
                } else {
                    chipYearly.visibility = View.GONE
                    yearly = "0"
                }

                if (monthly.equals("0") && quarterly.equals("0")
                    && halfYearly.equals("0") && yearly.equals("0")
                ) {
                    tvNoValidity.visibility = View.VISIBLE
                    chipGroup.visibility = View.GONE
                    btnSelect.isEnabled = false
                }

                btnSelect.setOnClickListener {
                    when (chipGroup.checkedChipId) {
                        R.id.chip_monthly -> listener.onItemClick(
                            "$monthly",
                            "${response?.packageName}\n${response?.pDescription}\nSelected Validity : ${chipMonthly.text}",
                            response.pDetials_Id
                        )

                        R.id.chip_quarterly -> listener.onItemClick(
                            "$quarterly",
                            "${response?.packageName}\n${response?.pDescription}\nSelected Validity : ${chipQuarterly.text}",
                            response.pDetials_Id
                        )

                        R.id.chip_halfYearly -> listener.onItemClick(
                            "$halfYearly",
                            "${response?.packageName}\n${response?.pDescription}\nSelected Validity : ${chipHalfYearly.text}",
                            response.pDetials_Id
                        )

                        R.id.chip_yearly -> listener.onItemClick(
                            "$yearly",
                            "${response?.packageName}\n${response?.pDescription}\nSelected Validity : ${chipYearly.text}",
                            response.pDetials_Id
                        )

                        else -> context.toast("Please select your validity !")
                    }
                }
            }
        }
    }
}
