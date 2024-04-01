/*
 * *
 *  * Created by Prady on 6/3/23, 11:44 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 6/3/23, 11:10 AM
 *
 */

package com.app.prady.appbase.ui.initialActivities.firstLoginActivity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.prady.appbase.R
import com.app.prady.appbase.databinding.ActivityFirstLoginBinding
import com.app.prady.appbase.helpers.PreferenceManager
import com.app.prady.appbase.ui.authActivity.AuthActivity
import java.util.*

class FirstLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstLoginBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstLoginBinding.inflate(layoutInflater)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(binding.root)

        preferenceManager = PreferenceManager.instance
        setupNavController()
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun setLanguage(language: String) {
        preferenceManager.languagePrefs = language
        val local = Locale(language)
        Locale.setDefault(local)
        val configuration = Configuration()
        configuration.setLocale(local)
        resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)
        this.recreate()
    }

    fun navigateToAuth() {
        val i = Intent(this, AuthActivity::class.java)
        startActivity(i)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}