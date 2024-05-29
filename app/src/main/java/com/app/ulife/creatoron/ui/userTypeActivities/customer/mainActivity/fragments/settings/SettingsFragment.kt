/*
 * *
 *  * Created by Prady on 4/8/23, 11:38 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/8/23, 11:21 AM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.FragmentSettingsBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.viewModels.SharedVM
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SettingsFragment : Fragment(), KodeinAware {
    private lateinit var binding: FragmentSettingsBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        setHasOptionsMenu(true)
//        (activity as MainActivity?)?.setToolbarVisibility(false)

        when (preferenceManager.languagePrefs) {
            "en" -> binding.radioEnglish.isChecked = true
            "hi" -> binding.radioHindi.isChecked = true
        }
    }

    private fun setupListeners() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = binding.root.findViewById(checkedId) as RadioButton
            when (radioButton.text as String) {
                getString(R.string.english) -> {
                    (activity as MainActivity?)?.setLanguage("en")
                }
                getString(R.string.hindi) -> {
                    (activity as MainActivity?)?.setLanguage("hi")
                }
            }
        }
    }
}
