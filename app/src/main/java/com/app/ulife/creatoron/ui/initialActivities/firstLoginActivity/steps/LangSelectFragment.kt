/*
 * *
 *  * Created by Prady on 6/3/23, 11:07 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 5/16/23, 2:08 PM
 *
 */

package com.app.ulife.creatoron.ui.initialActivities.firstLoginActivity.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.databinding.FragmentFirstStep1Binding
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.ui.initialActivities.firstLoginActivity.FirstLoginActivity

class LangSelectFragment : Fragment() {

    private lateinit var binding: FragmentFirstStep1Binding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        binding = FragmentFirstStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager.instance
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
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
                    (activity as FirstLoginActivity?)?.setLanguage("en")
                }
                getString(R.string.hindi) -> {
                    (activity as FirstLoginActivity?)?.setLanguage("hi")
                }
            }
        }

        binding.btnContinue.setOnClickListener {
//            val navBuilder = NavOptions.Builder()
//            navBuilder.setEnterAnim(R.anim.fade_in)
//                .setExitAnim(R.anim.fade_out)
//                .setPopEnterAnim(R.anim.fade_in).setPopExitAnim(R.anim.fade_out)
//            val args = Bundle()
//            args.putString("title", "")
//
//            findNavController().navigate(R.id.step2Fragment, args, navBuilder.build())
            (activity as FirstLoginActivity).navigateToAuth()
        }
    }
}