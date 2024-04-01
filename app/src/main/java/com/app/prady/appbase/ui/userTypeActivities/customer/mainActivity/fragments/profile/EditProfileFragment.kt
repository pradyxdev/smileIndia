/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/7/23, 3:05 PM
 *
 */

package com.app.prady.appbase.ui.userTypeActivities.customer.mainActivity.fragments.profile

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.app.prady.appbase.R
import com.app.prady.appbase.databinding.FragmentEditProfileBinding
import com.app.prady.appbase.factories.SharedVMF
import com.app.prady.appbase.helpers.Constants
import com.app.prady.appbase.helpers.PreferenceManager
import com.app.prady.appbase.models.EmptyRequest
import com.app.prady.appbase.models.UserIdRequest
import com.app.prady.appbase.models.session.SessionRes
import com.app.prady.appbase.models.userDetails.UserDetailsRes
import com.app.prady.appbase.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.prady.appbase.utils.LoadingUtils
import com.app.prady.appbase.utils.snackbar
import com.app.prady.appbase.viewModels.SharedVM
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import java.util.ArrayList
import kotlin.random.Random

class EditProfileFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentEditProfileBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    private val sessionNames: MutableList<String> = ArrayList()
    private var sessionNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
//        (activity as MainActivity?)?.setToolbarVisibility(true)
        binding.actSession.setText(preferenceManager.session)
        hitApis()
    }

    private fun hitApis() {
        getUserDetails(UserIdRequest("" + preferenceManager.userid))
    }

    private fun setupListeners() {
    }

    private fun getUserDetails(mobileNoReq: UserIdRequest) {
        LoadingUtils.showDialog(requireContext(), false)
        viewModel.getUserDetails = MutableLiveData()
        viewModel.getUserDetails.observe(requireActivity()) {
            val response = Gson().fromJson(it, UserDetailsRes::class.java)
//            Log.e("getUserDetails ", "$response")
            if (response != null) {
                if (response.status) {
                    LoadingUtils.hideDialog()

                    binding.apply {
                        etUsername.setText(response.response?.fullName)
                        etFather.setText(response.response?.fatherName)
                        etMother.setText(response.response?.motherName)
                        etAddress.setText(response.response?.address)

                        etEnrollNum.setText(response.response?.enrollmentNo)
                        etCourseName.setText(response.response?.courseName)

                        try {
                            val getDob = response.response?.dob
                            if (!getDob.isNullOrEmpty()) {
                                val terms = getDob.split("-")
                                etDay.setText(terms[0])
                                etMonth.setText(terms[1])
                                etYear.setText(terms[2])
                            }

                            val subjectList = response.response?.subjectName
                            if (!subjectList.isNullOrEmpty()) {
                                val chunks = subjectList.split(",")
                                for (i in chunks.indices) {
                                    Log.e("subjectsName ", "" + chunks[i])
                                    chipGroup.addView(
                                        createTagChip(
                                            requireContext(),
                                            chunks[i]
                                        )
                                    )
                                }
                            }

                        } catch (e: Exception) {
                            Log.e("getUserDetails ", "$e")
                        }
                    }
                } else {
                    LoadingUtils.hideDialog()
                    binding.root.snackbar("error: ${response.response}")
                }
            } else {
                LoadingUtils.hideDialog()
                (activity as MainActivity)?.apiErrorDialog(Constants.apiErrors)
            }
        }
        viewModel.getUserDetails(mobileNoReq)
    }

    private fun createTagChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = chipName
            setTextColor(ContextCompat.getColor(context, R.color.white))
            /* Creating a random color and setting it as the background color of the chip. */
//            val rnd = Random
//            val color: Int = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)) // any random color
            val color =
                Color.argb(
                    225,
                    Random.nextInt(180),
                    Random.nextInt(180),
                    Random.nextInt(180)
                ) // darker random color
            chipBackgroundColor = ColorStateList.valueOf(color)
            /* Setting the stroke color of the chip to transparent. */
            val transparentColor = Color.parseColor("#00ff0000")
            chipStrokeColor = ColorStateList.valueOf(transparentColor)
//            setChipBackgroundColorResource(R.color.purple_500)
//            isCloseIconVisible = true
//            setTextAppearance(R.style.ChipTextAppearance)
        }
    }
}